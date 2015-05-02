# Anicare
SEP521 Software Engineering Team 3 Project

ani-care
============
2015 SEP521 Term project

Project reference
---------------------
* facebook_lib
* google_play_services_lib

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
* PetListFragment : 친구 보기 Fra.
* MakeFriendFragment : 친구 만들기 Frag.
* MessageBoxFragment : 메세지함 Frag.
* CareHistoryFragment : 돌봄 이력 보기 Frag.
* SettingFragment : 설정창 보기 Frag.

sep.software.anicare.adapter
---------------------
* NavDrawerListAdapter : Drawer Adapter

sep.software.anicare.event
---------------------
* AniCareException : App에서 발생되는 Exception을 Wrapping한 클래스.
* AniCareMessage : GCM push 서버에서 전달되는 클래스.

sep.software.anicare.interfaces
---------------------
* EntityCallback : *``<T>``*의 객체를 전달 받을 수 있는 다용도 Callback 인터페이스.
* JsonCallback : [Gson](https://code.google.com/p/google-gson/)의 JsonElement를 전달 받는 Callback 인터페이스.
* ListCallback : *``List<T>``*의 객체를 전달 받을 수 있는 다용도 Callback 인터페이스.
* PairEntityCallback : *``<T>, <E>``* Pair를 전달 받을 수 있는 Callback 인터페이스.

sep.software.anicare.model
---------------------
* AniCareDateTime : 시간, 날짜 담당 클래스.
* AniCareUser : 해당 App의 사용자 클래스.
* Pet : 펫의 정보를 담는 클래스.

sep.software.anicare.service
---------------------
* AniCareService : 서버와의 통신을 담당하는 클래스.
* BlobStorageService : 에저 blob storage helper 클래스.

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

