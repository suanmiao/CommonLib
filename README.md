# Android Common Lib

## Introduction

 Home brew android common lib

## Feature
  **IO**
  > network module, compatible with both Volley and RoboSpice, all-in-one request management
  > image loading, both for items in scroll container and idle item,also blur image load and cache support
  > cache ,both on ram and disk

  **UI**
  > TextView and EditText with custom font support
  > MVVM framework on list item
  > Dialog customisation support (maybe more animation support in the future)

  **Request Framework**
  > full-case coverage for all request

  **Util**
  > Bitmap,Date,File utils

## Usage
####Step 1(installation):
**Gradle:**
```groovy
    compile 'me.suanmiao.common:library:0.1.6'
 ```

####Step 2(setup application):
* Create a "Application" class extends BaseApplication and set it to be main Application
```java
public class SApplication extends BaseApplication {

  @Override
  protected RequestManager initRequestManager() {
    return new RequestManager(this,".commonExample");
  }
}
```

```xml

    <application
        android:name="me.suanmiao.example.component.SApplication"
        ...
        >
```
####Step 3(usage of feature):
* Setup Volley Request
```java

    //setup request class extends CommonRequest
    public class ChannelRequest extends CommonRequest<ChannelModel> {
      ...
    }


    //execute request
    String url = "http://zhihurss.miantiao.me/zhihuzhuanlan/taosay";
    ChannelRequest request = new ChannelRequest(url);
    executeRequest(request, new VolleyCommonListener<ChannelModel>() {
      @Override
      public void onErrorResponse(VolleyError error) {
        //deal with request error
      }

      @Override
      public void onResponse(ChannelModel response) {
      //deal with result
     }
    });

```
* Setup RoboSpice Request



```java
  // setup Request Service
  public class RequestService extends RetrofitGsonSpiceService {

      private static OkHttpClient okHttpClient;

      @Override
      public void onCreate() {
          super.onCreate();
          addRetrofitInterface(APIService.class);
      }

      public static OkHttpClient getOkHttpClient();

      @Override
      protected String getServerUrl() {
          return APIConstants.BASE_URL;
      }
  }

  //setup request class
  public class SpiceExampleRequest extends BaseRoboRequest<ExampleItemModel, APIService> {
    public SpiceExampleRequest() {
      super(ExampleItemModel.class, APIService.class);
    }

    @Override
    public ExampleItemModel loadDataFromNetwork() throws Exception {
      return getService().getExample("token", "id");
    }
  }
  //execute request
  SpiceExampleRequest request1 = new SpiceExampleRequest();
  executeRequest(new SpiceBuilder<ExampleItemModel>().request(request1).build(),
      new SpiceCommonListener() {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(Object o) {
        }
      });

```

* Photo load static
```java
   Photo.loadImg(itemView.img,url, R.drawable.ic_launcher);
```

* Photo load in ListView
```java
public class ExampleItemViewModel extends BaseViewModel {
  ...

  @Override
  public void bind(int index, BaseModel baseModel, int scrollState, float scrollSpeed) {
    ...
    // load image when scroll
    Photo.loadScrollItemImg(itemView.img, model.img, R.drawable.ic_launcher, scrollState,
            scrollSpeed);
  }

  @Override
  public void idleReload() {
    ...
    // load image when list stop scrolling
    Photo.reloadImg(itemView.img);
  }
}

```


## Other
  **it's a library designed in my coding and feature style, and the stability hasn't been verified**

## Copyrights

Copyright 2014, suanmiao

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
