package com.fuicuiedu.xc.videonew_20170309.ui.base;

import android.content.Context;
import android.widget.FrameLayout;

/**
 *   自定义基类的单行布局——抽象的类
 */

public abstract class BaseItemView<Model> extends FrameLayout{

    /**
     * 带泛型的基类单行布局——BaseItemView<Model>
     * @param context
     */
    public BaseItemView(Context context) {
        super(context);
        initView();
    }

    //初始化当前视图
    protected abstract void initView();

    //将实体数据绑定到当前视图上
    protected abstract void bindModel(Model model);

}
