# [googlemvp](http://www.jianshu.com/p/ce1b447efc4f)
对原汁原味的google的mvp的实践
#前言
我根据在项目中使用过 mvp、mvp+databinding、mvp+Rxjava ，通过阅读google给出的mvp各种示例，对mvp进行总结实现mvp+databinding+Rxjava的实现方式；
# 我对于mvp-databinding-Rxjava的理解

我认为在android中M 就是Model 是操作数据的地方，在M 中可以调用各种处理数据的Manager，View 是activity，fragment等，在加上databinding可以在绑定数据，p是连接m与v之间的桥梁，以及控制一些需要在主线程实现的数据通过RxAndroid 切换到主线程中；我认为使用databinding的好处可以不用写findview，显示数据的时候直接set进去就可以了，不需要通过id在settext的方式，还有就是有一些数据是可以不需要切换ui线程，直接set就可以；而使用Rxjava的好处是可以不需要通过handler就可以实现对线程的切换，可以直接显示数据；


# 学习经历
在项目中使用过的mvp模式，也使用过mvvm模式，但是一直不知道怎么实现是对的，在使用过程中总是感觉有一些，感觉不舒服的地方，先说说我是用mvp的经历，主要是用过三种方式，最开始是使用zjutkz的一篇博客[选择恐惧症的福音！教你认清MVC,MVP和MVVM](http://zjutkz.net/2016/04/13/%E9%80%89%E6%8B%A9%E6%81%90%E6%83%A7%E7%97%87%E7%9A%84%E7%A6%8F%E9%9F%B3%EF%BC%81%E6%95%99%E4%BD%A0%E8%AE%A4%E6%B8%85MVC%EF%BC%8CMVP%E5%92%8CMVVM/),在这篇博客中我学会了如何使用mvp，发现这个模式实在的太美了，业务与页面完全分离，实现业务的时候只需要实现业务就可以，实现页面只需要实现自己的页面就好了，不需要关注太多，而且如果哪一天，经理说界面哪里不好看，需要改，直接修改页面就好了，完全不需要关注业务，如果要是说需求有变化，那么直接改变业务模块就可以了，而且各个模块之间尽量的实现了解耦，方便以后修改，但是在使用过程中感觉有点麻烦，之后又看了 **[MvpFrame](https://github.com/wolearn/MvpFrame)**一个比较简单的mvp架构，但是我发现在使用过程中对项目的侵入性很强，也有可能是我的强迫症，总是感觉没有真正的了解mvp，于是决定阅读google给出的mvp示例 [google](https://github.com/googlesamples/android-architecture) ,通过在项目中的使用，得出自己的mvp实现方式；

# 开始放码
[demo地址](https://github.com/jiangchunyu/googlemvp)
BasePresenter  中主要为了与Activity或者fragment中的生命周期统一，避免内存泄漏
```
public interface BasePresenter {    
  void onResume(Context context);  
  void onStop();    
  void onDestroy();
}
```
BaseView中只是对P进行绑定，如果要是在Activity中使用可以不去管这个方法；
```
public interface BaseView<T> {    void setPresenter(T presenter);}
```

BaseModel 对基础数据处理的实现,对于事件传递我使用了rxjava实现的Rxbus，不了解的可以看一下我之前的一篇rxbus的博客[RxJava实现事件总线 Rxbus代替eventbus 减少库的使用](http://www.jianshu.com/p/adaf05a4d7ad)
```
/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public abstract class BaseModel<T> {
    private Subscription rxMainBus;
    private Subscription rxThreadBus;
    protected Context mContext;
    //设置默认接受类型为全部接收
    private
    @EventType.ReceiveType
    int recieveType = EventType.REC_ALL;
    protected T mPresenter;

    public BaseModel(T mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * 进入
     */
    public void onResume(Context mContext) {
        this.mContext = mContext;
        initRxbus();
    }

    /**
     * 初始化接收类型
     *
     * @param recieveType
     */
    public void initReciever(@EventType.ReceiveType int recieveType) {
        this.recieveType = recieveType;
    }

    ;

    /**
     * 注册rxbus事件接收
     */
    private void initRxbus() {
        rxMainBus = RxBus.getDefault().toObserverable(RxEvent.class)
                .filter(new Func1<RxEvent, Boolean>() {
                    @Override
                    public Boolean call(RxEvent rxEvent) {
                        //此处可以通过Rxjava的filter过滤函数对数据进行过滤，从而得到自己想要的数据
                        if ((rxEvent.reciveType == recieveType ||
                                rxEvent.reciveType == EventType.REC_ALL) && (rxEvent.threadType ==
                                EventType.THREAD_ALL || rxEvent.threadType == EventType.THREAD_UI)) {
                            return true;
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//设置为主线程接收数据
                .subscribe(new Action1<RxEvent>() {
                               @Override
                               public void call(RxEvent rxEvent) {
                                   onMainEvent(rxEvent.eventAction, rxEvent.event);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                            }
                        });

        /**
         *
         */
        rxThreadBus = RxBus.getDefault().toObserverable(RxEvent.class)
                .filter(new Func1<RxEvent, Boolean>() {
                    @Override
                    public Boolean call(RxEvent rxEvent) {
                        //此处可以通过Rxjava的filter过滤函数对数据进行过滤，从而得到自己想要的数据
                        if ((rxEvent.reciveType == recieveType ||
                                rxEvent.reciveType == EventType.REC_ALL) && (rxEvent.threadType ==
                                EventType.THREAD_ALL || rxEvent.threadType == EventType.THREAD_CHILD)) {
                            return true;
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//设置为子线程接收数据
                .subscribe(new Action1<RxEvent>() {
                               @Override
                               public void call(RxEvent rxEvent) {
                                   onThreadEvent(rxEvent.eventAction, rxEvent.event);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                            }
                        });
    }


    public abstract void onMainEvent(String action, Object event);

    public abstract void onThreadEvent(String action, Object event);


    /**
     * 解除事件的注销，以保证不出现内存泄漏
     */
    public void onDestroy() {
        if (rxMainBus != null && !rxMainBus.isUnsubscribed()) {
            rxMainBus.unsubscribe();
        }
        if (rxThreadBus != null && !rxThreadBus.isUnsubscribed()) {
            rxThreadBus.unsubscribe();
        }
        mContext = null;
    }
}
```
还有两个我封装的基础类需要了解一下，不想看的这一步可以掠过嘿嘿，这两个基础类主要是通过封装减少代码；这个目前只是简单的封装以后还会对这些部分进一步的进行封装；
BaseActivity
```
public abstract class BaseActivity extends AppCompatActivity {
    private ViewDataBinding binding;

    protected abstract  int getLayoutId();

    protected abstract void initBinding(ViewDataBinding binding);

    protected abstract void initPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,getLayoutId());
        initBinding(binding);

    }

    @CallSuper
    @Override
    protected void onResume() {
        initPresenter();
        super.onResume();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

```
BaseDialog 
```
public abstract class BaseDialog extends Dialog implements Dialog.OnDismissListener{

    private Context mContext;
    private ViewDataBinding binding;

    public BaseDialog(Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    protected abstract int getLayoutId();

    protected abstract void initBinding(ViewDataBinding binding);

    protected abstract void initInfo(Object... objects);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),getLayoutId(),null,false);
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnDismissListener(this);
    }

    public void showDialog(){
        if(isShowing())return;
        show();
    }

    public void closeDialog(){
        if(!isShowing())return;
        dismiss();
    }
}

```

为防止代码量过大，我将会把具体实现方式放在[下一篇文章](http://www.jianshu.com/p/5bd2953143e5)里面给出，[google 官方mvp实例的实践之mvp-databinding-Rxjava（二）](http://www.jianshu.com/p/5bd2953143e5)

[demo地址](https://github.com/jiangchunyu/googlemvp)
