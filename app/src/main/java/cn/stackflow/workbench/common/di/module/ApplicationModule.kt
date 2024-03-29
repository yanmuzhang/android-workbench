package cn.stackflow.workbench.common.di.module

import com.king.frame.mvvmframe.di.module.ViewModelFactoryModule
import dagger.Module

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Module(includes = [ViewModelFactoryModule::class, ViewModelModule::class, ActivityModule::class, FragmentModule::class, DialogFragmentModule::class])
class ApplicationModule {

}