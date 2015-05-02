Anicare
============

## SEP521 Software Engineering Team 3 Project

Project reference
---------------------
* facebook_lib
* google_play_services_lib
* kakao sdk

sep.software.anicare
---------------------
* AniCareApp : AniCare Global Application Context, 각각의 helper 클래스들을 initialize하고 global function들이 정의됨.
* AniCareBroadCastReceiver : GCM push message를 받기 위한 리시버.
* AniCareIntentService : 브로드캐스트 리시버로 전달된 메세지를 실제 처리하는 서비스.
* AniCareProtocol : AniCare 프로젝트에 전체적인 Constant Variable이나 타입들을 정의.

sep.software.anicare.activity
---------------------
* SplashActivity : 처음 앱이 실행될때 보여지는 화면, 로그인 로직도 들어있음.
* UserSettingActivity : 사용자 세팅 변경 화면.
* PetSettingActivity : 반려동물 세팅 변경 화면.
* PetDetailActivity : 친구 자세히 보기 화면.
* MainActivity : App의 메인 Drawer Activity.

sep.software.anicare.fragment
---------------------
* ListFriendFragment : 친구 보기 Frag.
* MakeFriendFragment : 친구 만들기 Frag.
* MessageBoxFragment : 메세지함 Frag.
* CareHistoryFragment : 돌봄 이력 보기 Frag.
* SettingFragment : 설정창 보기 Frag.

sep.software.anicare.adapter
---------------------


sep.software.anicare.event
---------------------
* AniCareException : App에서 발생되는 Exception을 Wrapping한 클래스.
* AniCareMessage : GCM push 서버에서 전달되는 클래스.

sep.software.anicare.callback
---------------------
* EntityCallback : *``<T>``*의 객체를 전달 받을 수 있는 다용도 Callback 인터페이스.
* JsonCallback : [Gson](https://code.google.com/p/google-gson/)의 JsonElement를 전달 받는 Callback 인터페이스.
* ListCallback : *``List<T>``*의 객체를 전달 받을 수 있는 다용도 Callback 인터페이스.
* PairEntityCallback : *``<T>, <E>``* Pair를 전달 받을 수 있는 Callback 인터페이스.

sep.software.anicare.model
---------------------
* AniCareModel : AniCare Model 추상화 클래스. 특히 Parcelable에 관련된 API 제공
  ```java
  public void writeToParcel(Parcel dest, int flags);
  public static <E> E toClass(Parcel in, Class<E> clazz);
  ```
  ```writeToParcel()```함수는 ```override```되어 있고 CREATOR 객체를 만들때 ```toClass()``` 함수를 사용하면 편리하다. ([AniCareUser](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/model/AniCareUser.java#L58) 참조)
* AniCareDateTime : 시간, 날짜 담당 클래스.
* AniCareUser : 사용자 클래스
* AniCarePet : 펫 클래스

sep.software.anicare.service
---------------------
* AniCareService : 서버 통신 및 다양한 API를 제공해 주는 클래스.

sep.software.anicare.util
---------------------
* AniCareLogger : 여러 파라미터를 받는 간단한 Logger.
* AsyncChainer : Async 함수를 연결 시켜주는 Util.
* BitmapUtil : BitMap 관련 Util.
* ImageUtil : 이미지 Util.
* ObjectPreferenceUtil : 클래스 자체를 SharedPreference에 저장해주는 Util.
* RandomUtil : 랜덤값 Generator Util.

sep.software.anicare.view
---------------------
* CircleImageView : 이미지를 동그랗게 만들어줌.


sep.software.anicare.util.AsyncChainer
---------------------
  사용 예시
  ```java
  // 현재 context(mThisActivity)를 첫번째 param으로 넣고
  // 두번째 param부터는 연결하고픈 Async 함수들을 등록한다.
  AsyncChainer.asyncChain(mThisActivity, new Chainable(){

              @Override
              public void doNext(final Object key, Object... params) {
                  // TODO Auto-generated method stub
                  // 여기서 Async 함수를 호출한다.
                  asyncCall(new Callback(){
                      public void onCallback(){
                          // Async가 완료되면 다음 Chain에 연결된 Async 함수를 호출한다.
                          AsyncChainer.notifyNext(key);
                          // 만약 인자를 넘겨 주고 싶다면 다음과 같이도 가능하다.
                          // AsyncChainer.notifyNext(key, param1, param2);
                      }
                  });
              }

          }, new Chainable(){
              @Override
              public void doNext(final Object object, Object... params) {
                  // next asyncCall
              }
          });
  ```
[실제 사용 예시](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/activity/SplashActivity.java#L140)

AniCareActivity, AniCareFragment
---------------------
각각 Activity와 Fragment들의 최상위 클래스들이다. 크게 두가지 기능을 제공한다.
1. bindViews(), initialize() 추상 함수 선언
	* [상위 클래스 선언](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/activity/AniCareActivity.java#L62)
	* [하위 클래스 구현](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/activity/MainActivity.java#L57)
```java
// 하위 클래스에서 자바 view 객체와 xml을 바인딩하는 함수를 구현하게 강제한다.
protected void bindViews() {
	imageView = (ImageView) findViewById(R.id.imageView1);
    .....
}
// 나머지 초기화하게 만드는 함수를 구현하게 강제한다.
protected void initialize() {

}
```

2. 자주 쓰이는 객체들을 제공하여 하위 클래스에서 바로 사용할 수 있게 제공한다.
	* [see here](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/activity/AniCareActivity.java#L17)
```java
protected Activity mThisActivity;
protected Fragment mThisFragment;
protected AniCareApp mAppContext;
protected AniCareService mAniCareService;
protected ObjectPreferenceUtil mObjectPreference;
```


EventBus
---------------------
이벤트를 효율적으로 처리해주는 오픈소스인 [EventBus](https://github.com/greenrobot/EventBus)를
해당 프로젝트에서 크게 두가지로 사용하면 좋을 것 같다.
1. 에러 처리 Handler 로직
* [AniCareServiceAzure](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/service/AniCareServiceAzure.java#L99) 예시
```java
mobileClient.invokeApi("send_message", new Gson().fromJson(message.toString(), JsonElement.class), new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                // 정상적으로 Callback 될때.
                if (arg1 == null) {
                    callback.onCompleted(new Gson().fromJson(arg0, AniCareMessage.class));
                // Exception이 일어났을 때.
                } else {
					// EventBus로 이벤트를 날려준다.
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }

            }
        });
    }
```

* 기본적으로 모든 에러 처리는 [AniCareActivity](https://github.com/Jeffrey-Cho/Anicare/blob/master/app/src/main/java/sep/software/anicare/activity/AniCareActivity.java#L37)에서 담당하고 있다. 원한다면 ```onEvent()```함수를 override하여 customize할 수 있다.
```java
public void onEvent(AniCareException exception){
        

    AniCareAlertDialog.newInstance(exception.getType().toString(), "OK", "Cancel", true).setCallback(new DialogCallback() {

        @Override
        public void doPositive(Bundle bundle) {
        // TODO Auto-generated method stub

        }

        @Override
        public void doNegative(Bundle bundle) {
        // TODO Auto-generated method stub

        }
    }).show();
}
```
