package cn.stackflow.workbench.common.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.stackflow.workbench.R
import cn.stackflow.workbench.ui.Constants
import cn.stackflow.workbench.ui.adapter.BaseBindingAdapter
import cn.stackflow.workbench.databinding.ListFragmentBinding
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.list_fragment.*

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
abstract class ListFragment<T, VM : ListViewModel<T>> : BaseFragment<VM, ListFragmentBinding>() {

    var curPage : Int = 1
    var pageSize = Constants.PAGE_SIZE

    lateinit var mAdapter : BaseBindingAdapter<T>

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        viewModel?.let {
            pageSize = it.pageSize()
        }
        initRecyclerView(rv)
        mAdapter = createAdapter()
        rv.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position -> clickItem(mAdapter.getItem(position))}
        observeData()
        initRefreshLayout(srl)
    }

    open fun initRecyclerView(rv: RecyclerView){
        rv.layoutManager = LinearLayoutManager(context)
        rv.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
    }

    open fun initRefreshLayout(srl: SmartRefreshLayout){
        srl.setEnableLoadMore(false)
        srl.setOnRefreshListener{requestData(1)}
        srl.setOnLoadMoreListener {requestData(curPage)}
        srl.autoRefresh()
    }

    open fun observeData(){
        viewModel.liveData.observe(this, Observer{ t -> updateUI(t,curPage > 1) })
    }

    fun requestData(page: Int){
        curPage = page
        viewModel?.requestData(curPage)
    }

    override fun hideLoading() {
        super.hideLoading()
        srl.closeHeaderOrFooter()
        initEmptyView()
    }

    private fun initEmptyView(){
        if(mAdapter.emptyLayout == null){
            createEmptyView(rv)?.let {
                mAdapter.setEmptyView(it)
            }
        }
    }

    open fun createEmptyView(root: ViewGroup): View? {
        return inflate(R.layout.layout_empty,root,false)
    }

    override fun getLayoutId(): Int {
        return R.layout.list_fragment
    }

    open fun clickItem(data: T){

    }

    open fun updateUI(data: Collection<T>?,loadMore: Boolean){
        data?.let {
            if(loadMore) mAdapter.addData(data) else mAdapter.setList(data)

            if(mAdapter.itemCount >= curPage * pageSize){
                srl.setEnableLoadMore(true)
                curPage++
            }else{
                srl.setEnableLoadMore(false)
                srl.finishLoadMoreWithNoMoreData()
            }
        }
    }

    abstract fun createAdapter(): BaseBindingAdapter<T>
}