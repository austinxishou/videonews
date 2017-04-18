package com.fuicuiedu.xc.videonew_20170309.ui.likes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LikesFragment extends Fragment implements RegisterFragment.OnRegisterSuccessListener, LoginFragment.OnLoginSuccessListener {

    /** 用户名——登录——注册——退出登录——分割线——————— —自定义的listview*/
    @BindView(R.id.tvUsername)
    TextView mTvUsername;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    @BindView(R.id.btnLogout)
    Button mBtnLogout;

    @BindView(R.id.divider)
    View mDivider;
    @BindView(R.id.likesListView)
    LikesListView likesListView;


    /**登录注册的界面*/
    private View view;
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_likes, container, false);
            ButterKnife.bind(this, view);
            /**拿到单例的用户类，判断用户登录状态，更新UI*/
            UserManager userManager = UserManager.getInstance();
            /**判断用户的信息是否存入了用户的管理类中，如果有用户信息，就直接登录————调用登录的方法（userOnLine）*/
            if (!userManager.isOffline()) {
                userOnLine(userManager.getUsername(), userManager.getObjectId());
            }
        }
        return view;
    }

    /**按钮的监听*/
    @OnClick({R.id.btnRegister, R.id.btnLogin, R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            /**注册*/
            case R.id.btnRegister:
                if (mRegisterFragment == null) {
                    mRegisterFragment = new RegisterFragment();
                    /**添加注册成功的监听——————————————————————————————————设置fragemnt的监听*/
                    mRegisterFragment.setListener(this);
                }
                /**如果对象已经创建了，就直接实现dialogfragment*/
                mRegisterFragment.show(getChildFragmentManager(), "Register Dialog");
                break;
          /**登录*/
            case R.id.btnLogin:
                if (mLoginFragment == null) {
                    mLoginFragment = new LoginFragment();
                    /**添加登录成功的监听——————————————————————————————————设置fragemnt的监听*/
                    mLoginFragment.setListener(this);
                }
                /**如果对象已经创建了，就直接实现dialogfragment*/
                mLoginFragment.show(getChildFragmentManager(), "Login Dialog");
                break;
            /**退出登录*/
            case R.id.btnLogout:
                /**用户下线的操作*/
                userOffline();
                break;
        }
    }

    /**——————————————————————————————————————————————————————————————————————————————————————————————
     * 添加注册成功的监听*/
    @Override
    public void registerSuccess(String username, String objectId) {
        /**登录成功后——关闭注册的对话框——直接执行用户上线的操作*/
        mRegisterFragment.dismiss();
        /**用户上线*/
        userOnLine(username, objectId);
    }

    /**登录成功*/
    @Override
    public void loginSuccess(String username, String objectId) {
        mLoginFragment.dismiss();
        /**用户上线*/
        userOnLine(username, objectId);
    }
    /**——————————————————————————————————————————————————————————————————————————————————————————————
    * */
    /**用户上线*/
    /**如果用户的管理类中有保存的用户名和id————就显示用户上线时候的界面*/
    private void userOnLine(String username, String objectId) {
     /**登录注册还有分割线设置为隐藏——退出登录按钮显示*/
        mBtnLogin.setVisibility(View.INVISIBLE);
        mBtnRegister.setVisibility(View.INVISIBLE);
        mDivider.setVisibility(View.INVISIBLE);
        mBtnLogout.setVisibility(View.VISIBLE);

        mTvUsername.setText(username);
        /**存储用户信息——用户名和id*/
        UserManager.getInstance().setUsername(username);
        UserManager.getInstance().setObjectId(objectId);
        /**刷新收藏列表*/
        likesListView.autoRefresh();
    }

    /**用户下线*/
    private void userOffline() {
        /**清除用户相关信息*/
        UserManager.getInstance().clear();
        /**更新UI*/
        mBtnLogin.setVisibility(View.VISIBLE);
        mBtnRegister.setVisibility(View.VISIBLE);
        mBtnLogout.setVisibility(View.INVISIBLE);
        mDivider.setVisibility(View.VISIBLE);
        mTvUsername.setText(R.string.tourist);
        /**清空收藏列表*/
        likesListView.clear();
    }

}
