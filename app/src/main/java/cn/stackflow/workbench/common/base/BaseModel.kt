package cn.stackflow.workbench.common.base

import com.king.frame.mvvmframe.base.BaseModel
import com.king.frame.mvvmframe.data.IDataRepository
import javax.inject.Inject

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class BaseModel @Inject constructor(dataRepository : IDataRepository) : BaseModel(dataRepository) {

}