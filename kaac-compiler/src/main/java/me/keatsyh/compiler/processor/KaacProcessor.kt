package me.keatsyh.compiler.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*

import me.keatsyh.annotation.BindRepo
import me.keatsyh.annotation.BindVM
import me.keatsyh.compiler.*
import me.keatsyh.compiler.extended.classifyWithEnclosing
import me.keatsyh.compiler.utils.Logger
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.util.Elements
import java.util.*
import javax.lang.model.element.*
import javax.lang.model.util.Types


abstract class KaacProcessor : AbstractProcessor() {

    protected lateinit var elementUtils: Elements
    protected lateinit var typeUtils: Types
    protected lateinit var logger: Logger
    protected lateinit var formatModuleName: String

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elementUtils = processingEnv.elementUtils
        typeUtils = processingEnv.typeUtils
        val messager = processingEnv.messager
        logger = Logger.init(messager)
        val options = processingEnv.options
        if (options.isNotEmpty()) {
            val mOriginalModuleName = options[MODULE_NAME] ?: ""
            formatModuleName = mOriginalModuleName.replace("[^0-9a-zA-Z_]+".toRegex(), "")
            logger.w("[$mOriginalModuleName] $formatModuleName init")
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        generate(annotations, roundEnv)
        return true
    }

    abstract fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment)


    fun FileSpec.writeFile() {

        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")
        logger.w("$kaptKotlinGeneratedDir")
        val outputFile = File(kaptKotlinGeneratedDir).apply {
            mkdirs()
        }
        writeTo(outputFile.toPath())
    }

}

@AutoService(Processor::class)
@SupportedOptions(MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = [ANNOTATION_TYPE_BINDVM])
class BindVMProcessor : KaacProcessor() {

    private val classifyElement: MutableMap<TypeElement, MutableList<Element>> = HashMap()

    override fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
        val elementSet = roundEnv.getElementsAnnotatedWith(BindVM::class.java)
        elementSet.classifyWithEnclosing(classifyElement)
        classifyElement.forEach { enclosing, elements ->
            //            logger.w("packageName ${enclosing.asClassName().packageName}")
//            logger.w("${enclosing.kind}")
            if (enclosing.kind == ElementKind.CLASS) {
                val bindVMFileSpec = FileSpec.builder(enclosing.asClassName().packageName, fileNameOfSimpleName(enclosing.simpleName.toString()))
                        .addImport(EXTENDED_PACKAGE, "getKaac")
                val injectFunSpecBuild = FunSpec.builder(INJECT_FUN)
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("instance", Any::class)

                elements.forEach {
                    val bindVM = it.getAnnotation(BindVM::class.java)
                    injectFunSpecBuild.addStatement("(instance as %T).%L = getKaac().viewModelFactory.create(%L::class.java)",
                            enclosing.asType().asTypeName(),
                            it.simpleName,
                            bindVM.name)
                }

                val bindVMClassSpec = TypeSpec.classBuilder(fileNameOfSimpleName(enclosing.simpleName.toString()))
                        .addSuperinterface(ClassName.bestGuess(INJECT_PATH))
                        .addFunction(injectFunSpecBuild.build())
                        .build()
                bindVMFileSpec.addType(bindVMClassSpec)
                bindVMFileSpec.build().writeFile()
            }
        }
    }
}


@AutoService(Processor::class)
@SupportedOptions(MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = [ANNOTATION_TYPE_BINDREPO])
class BindRepoProcessor : KaacProcessor() {

    private val classifyElement: MutableMap<TypeElement, MutableList<Element>> = HashMap()
    override fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
        val elementSet = roundEnv.getElementsAnnotatedWith(BindRepo::class.java)

        elementSet.classifyWithEnclosing(classifyElement)
        classifyElement.forEach { enclosing, elements ->
//                        logger.w("packageName ${enclosing.asClassName().packageName}")
//            logger.w("${enclosing.kind}")
            if (enclosing.kind == ElementKind.CLASS) {
                val bindVMFileSpec = FileSpec.builder(enclosing.asClassName().packageName, fileNameOfSimpleName(enclosing.simpleName.toString(), "BindRepo"))
                        .addImport(EXTENDED_PACKAGE, "getKaac")
                val injectFunSpecBuild = FunSpec.builder(INJECT_FUN)
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("instance", Any::class)
                elements.forEach {
//                     val element = it as VariableElement
                    logger.w("RepoName: $enclosing ${it.simpleName}")
                    val bindVM = it.getAnnotation(BindRepo::class.java)
                    injectFunSpecBuild.addStatement("(instance as %T).%L = %L()",
                            enclosing.asType().asTypeName(),
                            it.simpleName,
                            bindVM.name)
                }

                val bindVMClassSpec = TypeSpec.classBuilder(fileNameOfSimpleName(enclosing.simpleName.toString(),"BindRepo"))
                        .addSuperinterface(ClassName.bestGuess(INJECT_PATH))
                        .addFunction(injectFunSpecBuild.build())
                        .build()
                bindVMFileSpec.addType(bindVMClassSpec)
                bindVMFileSpec.build().writeFile()
            }
        }
    }
}








