package me.keatsyh.demo;


import me.keatsyh.annotation.BindVM;
import me.keatsyh.kaac.layout.KActivity;
import timber.log.Timber;


public class HomeActivity extends KActivity {

    @BindVM(name = "TestVM")
    TestVM testVM;

    @Override
    public int asbLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void createViewModel() {
//        writeData("123");
        getKaac().createVM(this);
        Timber.d("TestVM: "+testVM);
    }


//    @PermissionAllow(value = {WRITE_EXTERNAL_STORAGE, CAMERA}, requestCode = 2)
//    public void writeData(String string) {
//        Timber.d("HomeActivity  %s", string);
//    }
//
//    @PermissionRefuse()
//    public void refuse(RefuseBean refuseBean) {
//        Timber.d("HomeActivity  refuse %s",  refuseBean);
//    }
//
//    @PermissionProhibit()
//    public void prohibit(ProhibitBean prohibitBean) {
//        Timber.d("HomeActivity  prohibit %s",  prohibitBean);
////        Bu builder = AlertDialog.Builder(this)
////        builder.setMessage("使用该功能需要使用SD卡权限\n是否再次开启权限")
////        builder.setPositiveButton("是") { dialog, which -> toSetting() }
////        builder.setNegativeButton("否", null)
////        builder.setCancelable(true)
////        builder.show()
//    }
}
