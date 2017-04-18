package com.fuicuiedu.xc.videonew_20170309.ui.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.bombapi.BombClient;
import com.fuicuiedu.xc.videonew_20170309.bombapi.NewsApi;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.QueryResult;
import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  带下拉刷新及分页加载功能的自定义视图
 * <p>
 * 本API已完成列表视图上下拉获取数据 及 使用适配器适配显示数据的核心业务流程
 * <p>
 * 子类只需重写 queryData(),getLimit()和createItemView(),分别去获取不同数据及创建不同列表项视图即可
 * <p>
 *  列表视图使用 {@link RecyclerView}实现
 * <p>
 * 下拉刷新使用 {@link SwipeRefreshLayout}实现
 * <p>
 * 分页加载使用 {@link Mugen} + {@link ProgressBar} 实现
 * <p>
 * 数据获取使用 {@link NewsApi}实现
 * <p>
 */


/**
 * 抽象的类————传入泛型————实体类和不同的单行布局————（实现了刷新和mugen来分页加载）
 */
public abstract class BaseResourceView<Model, ItemView extends BaseItemView<Model>>
        extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener, MugenCallbacks {


    /**自定义的3个构造方法——最后一个初始化视图*/
    public BaseResourceView(Context context) {
        this(context, null);
    }

    public BaseResourceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseResourceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**——————————————————————————————————————————————————————————————————————————————————————————————————————————
     * recyclerview和SwipeRefreshLayout和加载更多时候的进度条progressbar
     */
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    /**跳过多少条数据*/
    private int skip = 0;
    /**布尔值——是否已经加载完所有数据（limit VS 服务器返回的数据量）*/
    private boolean loadAll;
    /**自定义的数据适配器*/
    protected ModelAdapter adapter;
    /**新闻的api接口*/
    protected NewsApi newsApi;


    /**———————————————————————————————————继承baseitemvie要实现的抽象方法—————————————————————————————————————————————————
     * 初始化视图*/
    private void initView() {
        /**NewsApi初始化*/
        newsApi = BombClient.getInstance().getNewsApi();
        LayoutInflater.from(getContext()).inflate(R.layout.partial_pager_resource, this, true);
        ButterKnife.bind(this);

        /**初始化RecyclerView*/
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ModelAdapter();
        recyclerView.setAdapter(adapter);


        /**SwipeRefreshLayout——配置下拉刷新——设置刷新图标的颜色*/
        /**是可以填入好几种进度条的颜色——类似转一圈换一种颜色*/
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);


        /**配置上拉加载——使用mugen（第二个参数，是使用mugen所需要的接口）**/
        Mugen.with(recyclerView, this).start();
    }


    /**自动刷新的功能——————————比如添加一个按钮来实现刷新的功能*/
    public void autoRefresh() {
        /**显示刷新的图标*/
        refreshLayout.setRefreshing(true);
        /**调用自定刷新*/
        onRefresh();
    }

    /**刷新触发的方法*/
    @Override
    public void onRefresh() {
        /**拿到获取数据的请求call——————并且用limit的值来限制获取每次获取网络数据的条数*/
        Call<QueryResult<Model>> call = queryData(getLimit(),0);
        /**返回null，说明查询条件不足*/
        if (call == null){
            refreshLayout.setRefreshing(false);//没拿到call的时候————下拉刷新停止
            return;
        }
        /**拿到call——异步执行(传入带泛型的实体类——里面封装了网络数据的集合)*/
        call.enqueue(new Callback<QueryResult<Model>>() {
            @Override
            public void onResponse(Call<QueryResult<Model>> call, Response<QueryResult<Model>> response) {
                /**停止刷新——隐藏进度条的图标*/
                refreshLayout.setRefreshing(false);
               /**从call的异步执行——拿到响应中的数据集合——并用对象接收*/
                List<Model> datas = response.body().getResults();
                /**改变跳过数目（skip）————且datas.size就等于limit的值*/
                skip = datas.size();
                /**判断是否获取完服务器的数据——如果获取到的数据的个数小于limit的值，代表没数据了*/
                loadAll = datas.size() < getLimit();
                /**将数据添加到adapter——调用刷新的方法，清空旧数据，并且更新了适配器*/
                adapter.clear();
                /**将传进来的数据强转成带泛型的集合*/
                adapter.addData((ArrayList<Model>) datas);
            }

            /**请求失败的时候，让刷新停止————吐司错误的原因*/
            @Override
            public void onFailure(Call<QueryResult<Model>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                ToastUtils.showShort("onFailure:" + t.getMessage());
            }
        });
    }

    /**————————————————————————————————————————————————————————————————————————————————————————————
     * mugen上拉加载时会触发的方法
     */


    /**加载更多的方法——onLoadMore*/
    @Override
    public void onLoadMore() {
        /**拿到获取数据的请求call——传入每次获取信息的条数，每次跳过skip数量的数据*/
        Call<QueryResult<Model>> call = queryData(getLimit(),skip);
        /**返回null，说明查询条件不足*/
        if (call == null){
            ToastUtils.showShort("查询条件异常");
            return;
        }


          //TODO————————-在底部添加一个布局，显示查看更多和一个进度条转动
          //TODO————————-在底部添加一个布局，显示查看更多和一个进度条转动
          //TODO————————-在底部添加一个布局，显示查看更多和一个进度条转动
          //TODO————————-在底部添加一个布局，显示查看更多和一个进度条转动


        /**显示加载视图————————加载的时候显示进度条*/
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<QueryResult<Model>>() {
            @Override
            public void onResponse(Call<QueryResult<Model>> call, Response<QueryResult<Model>> response) {
                /**隐藏加载视图*/
                progressBar.setVisibility(View.INVISIBLE);
                /**拿到响应数据*/
                List<Model> datas = response.body().getResults();
                /**加载更多的时候——改变跳过数目（skip）*/
                skip = skip + datas.size();
                /**是否获取完服务器数据——如果获取到的数据个数小于限制数limit，代表取完了*/
                loadAll = datas.size() < getLimit();
                /**将数据添加到adapter*/
                adapter.addData((ArrayList<Model>) datas);
            }

            /**如果加载失败——隐藏加载进度条——吐司错误信息*/
            @Override
            public void onFailure(Call<QueryResult<Model>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);//
                ToastUtils.showShort("onFailure:" + t.getMessage());
            }
        });
    }

    /**根据进度条是否显示，来判断当前是否正在加载数据*/
    @Override
    public boolean isLoading() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    /**根据布尔值loadAll——判断是否还有数据*/
    @Override
    public boolean hasLoadedAllItems() {
        return loadAll;
    }
    /**--------------------------上拉加载时会触发的方法---------------------*/



    /**抽样方法——从服务器查询数据(就是构建一个请求)*/
    protected abstract Call<QueryResult<Model>> queryData(int limit, int skip);
   
    /**每页从服务器获取多少条数据——子类实现传入int数值*/
    protected abstract int getLimit();

    /**每个单项数据的视图——带返回类型的抽象方法——子类实现需要填充子类的单向布局*/
    protected abstract ItemView createItemView();




    /**________________________________________________________________________________________________________________________
     * RecyclerView的数据适配器_______不确定单行布局实体类数据的类型（泛型）*/
    protected class ModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Model> dataSet = new ArrayList<>();


        /**普通方法——清除集合中数据之后通知适配器更新*/
        public void clear() {
            dataSet.clear();
            notifyDataSetChanged();
        }

        /**添加满的集合数据到集合中*/
        public void addData(ArrayList<Model> data) {
            dataSet.addAll(data);
            notifyDataSetChanged();
        }

        /**oncreatview方法——需要填充布局*/
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /**抽象方法——子类实现（传入布局）*/
            ItemView itemView = createItemView();

            /**将itemView视图填充到布局中————因为itemview是继承framelayout，所有有setlayoutparams方法*/
            /**setlayoutparams方法中——最好传入父布局的LayoutParams（此处的itemview的父布局是RecyclerView）*/
            itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            /**使用recyclerview自带的viewholder对象————需要传入单行布局到viewholder中*/
            return new RecyclerView.ViewHolder(itemView) {};
        }


        /**绑定view的方法————onbindviewholder*/
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            /**Model是实体类————需要拿到当前项的实体类*/
            Model model = dataSet.get(position);
            /**当前项的视图itemView——因为不知道itemiew中有什么控件，所以让itemview实现抽象方法bindModel*/
            ItemView itemView = (ItemView) holder.itemView;
            /**将当前项的数据（泛型实体类model）设置到当前项的视图上*/
            itemView.bindModel(model);
        }
        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }


}
