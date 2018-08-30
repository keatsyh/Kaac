package me.keatsyh.compiler.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import me.keatsyh.annotation.PermissionAllow
import me.keatsyh.annotation.PermissionProhibit
import me.keatsyh.annotation.PermissionRationale
import me.keatsyh.annotation.PermissionRefuse
import me.keatsyh.annotation.RuntimePermission
import me.keatsyh.compiler.*
import me.keatsyh.compiler.extended.getAnnotation
import me.keatsyh.compiler.extended.getChildElement
import me.keatsyh.compiler.extended.toUpperCase
import java.util.HashMap
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import me.keatsyh.compiler.extended.toPropertyName
import javax.tools.StandardLocation


@AutoService(Processor::class)
@SupportedOptions(MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = [ANNOTATION_TYPE_PERMISSION_ALLOW,
    ANNOTATION_TYPE_PERMISSION_REFUSE,
    ANNOTATION_TYPE_PERMISSION_PROHIBIT,
    ANNOTATION_TYPE_PERMISSION_RATIONALE,
    ANNOTATION_TYPE_RUNTIME_PERMISSION])
class PermissionAllowProcessor : KaacProcessor() {
    private val allowClassify: MutableMap<TypeElement, MutableList<Element>> = HashMap()
    private val refuseClassify: MutableMap<TypeElement, MutableList<Element>> = HashMap()
    private val prohibitClassify: MutableMap<TypeElement, MutableList<Element>> = HashMap()
    private val rationaleClassify: MutableMap<TypeElement, MutableList<Element>> = HashMap()
    private val permissionFileSpecMap: HashMap<String, FileSpec.Builder> = HashMap()
    private val permissionClassSpecMap: HashMap<String, TypeSpec.Builder> = HashMap()


    private lateinit var contextCompatType: TypeElement
    private lateinit var packageManager: TypeElement
    private lateinit var activityManager: TypeElement
    private lateinit var applicationType: TypeElement
    private lateinit var filer: Filer


    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        val elementUtils = processingEnv.elementUtils
        contextCompatType = elementUtils.getTypeElement(ANDROID_TYPE_CONTEXTCOMPA)
        packageManager = elementUtils.getTypeElement(ANDROID_TYPE_PACKAGEMANAGER)
        activityManager = elementUtils.getTypeElement(ANDROID_TYPE_ACTIVITYCOMPAT)
        applicationType = elementUtils.getTypeElement(ANDROID_TYPE_APPLICATION)
        filer = processingEnv.filer
    }


    override fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
        roundEnv.processingOver()

//        val stream = roundEnv.rootElements.stream()
//        stream.forEach {
//            logger.w("stream  $it")
//        }

//        logger.w("SOURCE_PATH  ${StandardLocation.SOURCE_PATH}   ${filer.}")


        val runtimeElementSet = roundEnv.getElementsAnnotatedWith(RuntimePermission::class.java)
        val allowElementSet = roundEnv.getElementsAnnotatedWith(PermissionAllow::class.java)
        val refuseElementSet = roundEnv.getElementsAnnotatedWith(PermissionRefuse::class.java)
        val prohibitElementSet = roundEnv.getElementsAnnotatedWith(PermissionProhibit::class.java)
        val rationaleElementSet = roundEnv.getElementsAnnotatedWith(PermissionRationale::class.java)

        logger.w("runtimeElementSet:  ${runtimeElementSet.toString()}")
        runtimeElementSet.forEach { parent ->
            val fullName = parent.toString()
            val pkg = fullName.substring(0, fullName.lastIndexOf("."))
            val fileName = fullName.substring(fullName.lastIndexOf(".") + 1)
            logger.w("Pak  $pkg   fileName  $fileName")


//            val fileObject = filer.getResource(StandardLocation.SOURCE_PATH, pkg, "$fileName.java")
//            val openInputStream = fileObject.openInputStream()
//            val bufferedReader = openInputStream.bufferedReader()
//            val readText = bufferedReader.readText()
//            logger.w("readText:  $readText")


            parent as TypeElement
            val allowElement = parent.getChildElement<PermissionAllow, ExecutableElement>()
            val refuseElement = parent.getChildElement<PermissionRefuse, ExecutableElement>()
            val prohibitElement = parent.getChildElement<PermissionProhibit, ExecutableElement>()
            val rationaleElement = parent.getChildElement<PermissionRationale, ExecutableElement>()
//
            val permissionFileSpec = FileSpec.builder(parent.asClassName().packageName
                    , fileNameOfSimpleName(parent.simpleName.toString()
                    , "Permission"))
                    .addImport(EXTENDED_PACKAGE, "getApp")
//
            val permissionClassSpec = TypeSpec.classBuilder(fileNameOfSimpleName(parent.simpleName.toString()
                    , "Permission"))
                    .addSuperinterface(ClassName.bestGuess(IPERMISSION_PATH))
//
            allowElement.forEach {
                val regisPermissionFunSpec = FunSpec.builder("regisPermission")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("instance", Any::class)
                        .build()

                val checkAllowFunSpec = createFunSpec(parent, it)
                val propertySpec = createPropertySpec(it)
                permissionFileSpec.addProperty(propertySpec)
                permissionFileSpec.addFunction(checkAllowFunSpec)
                permissionClassSpec.addFunction(regisPermissionFunSpec)
            }

            val classSpec = permissionClassSpec.build()
            permissionFileSpec.addType(classSpec)
            permissionFileSpec.build().writeFile()
        }
    }



    fun createFunSpec(parent: TypeElement, e: ExecutableElement): FunSpec {
        val hasPermission = ClassName("me.keatsyh.kaac.permission", "hasPermission")
        val funName = e.simpleName.toString().toUpperCase(0, 1)
        return FunSpec.builder("check$funName")
                .receiver(parent.asClassName())
                .beginControlFlow("if (getApp<%T>().%T(*%L))", applicationType, hasPermission, e.toPropertyName())
                .addCode(CodeBlock.builder().addStatement(
                        "", ""
                ).build())
                .endControlFlow()
                .build()
    }


    fun createPropertySpec(e: ExecutableElement): PropertySpec {

        val permissionValue: Array<out String> = e.getAnnotation<PermissionAllow>().value
        val formattedValue: String = permissionValue.joinToString(
                separator = ",",
                transform = { "\"$it\"" }
        )

        val stringArrayType = ARRAY.parameterizedBy(String::class.asTypeName())
        return PropertySpec.builder(e.toPropertyName(), stringArrayType, KModifier.PRIVATE)
                .initializer("%N", "arrayOf($formattedValue)")
                .build()
    }
}


/**
 *
 * 1、创建注解处理文件
 * 2、创建注解处理类实现 IPermission
 * 3、拿到 @PermissionAllow 的值
 * 4、创建检查权限的方法，创建 bussion 方法，并在内部调用被 @PermissionAllow 的方法
 *      1、有权限直接调用 bussio 方法
 *      2、无权限创建 shouldShowRequestPermissionRationale 方法
 *      3、在 shouldShowRequestPermissionRationale 判断内部调用 PermissionRationale
 *      4、如果为 false 的话在 创建 requestPermissions 方法并调用
 *
 * 5、创建 onRequestPermissionsResult 方法
 *      1、创建 权限的成功、拒绝、禁止判断
 *      2、在成功的判断下调用被 @PermissionAllow 的方法
 *      3、在拒绝的判断下调用被 @PermissionRefuse 的方法
 *      4、在禁止的判断下调用被 @PermissionProhibit 的方法
 *
 */


//@AutoService(Processor::class)
//@SupportedOptions(MODULE_NAME)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes(value = [ANNOTATION_TYPE_PERMISSION_REFUSE])
//class PermissionRefuseProcessor : KaacProcessor() {
//
//
//    override fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
//
//    }
//
//}
//
//
//@AutoService(Processor::class)
//@SupportedOptions(MODULE_NAME)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes(value = [ANNOTATION_TYPE_PERMISSION_PROHIBIT])
//class PermissionProhibitProcessor : KaacProcessor() {
//
//
//    override fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
//
//
//    }
//}
//
//
//@AutoService(Processor::class)
//@SupportedOptions(MODULE_NAME)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes(value = [ANNOTATION_TYPE_PERMISSION_RATIONALE])
//class PermissionRationaleProcessor : KaacProcessor() {
//
//
//    override fun generate(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
//
//
//    }
//
//}