## 组件使用说明

- 必须使用seajs引入
- 仅遵循使用说明使用


### common
- 公用小功能组件
- 目前功能
	- 统一的回退事件

### loading
- 加载loading事件
- 使用方法

```javascript

var loading = new Loading();
loading.show();//显示
loading.hide();//隐藏

```

### tab
- tab切换组件
- 使用方法

```html

<div class="J_TabBar">
	<a href="javascript:;">1</a>
	<a href="javascript:;">2</a>
	<a href="javascript:;">3</a>
</div>
<div class="J_TabContent">
	<div>111111</div>
	<div>222222</div>
	<div>333333</div>
</div>

```

```javascript

new Tab('.J_TabContent', '.J_TabBar');

```

### toast
- toast弱提示
- 使用方法

```javascript

let duration = 2000; //展示时间 默认 2000ms
Toast('demo文案', duration);

```

### pageCalendar
- 全屏日历组件
- 参数繁多，只列举常用的
- 使用方法

```javascript

//单程
var __calendar = new PageCalendar({
	dateRange: ['2017-08-01', '2018-08-01'],
	selectRange: false,
    // useTransform: /Android/.test(navigator.userAgent) ? false : true,
    selectSameDay: false,
    hideAfterSelected: true,
    maxRange: 30,
    maxRangeNote: '亲，往返机票只能预定30天',
    selectRangeNote: ['请选择出发日期', '请选择返程日期'],
    selectedRangeCallback: function(e){
    	console.log(e);
	}
    // navigateByHash: that.isHash
});
__calendar.render();
__calendar.show();

//往返
var __calendar = new PageCalendar({
	dateRange: ['2017-08-01', '2018-08-01'],
	selectedNote: ['去程', '返程'],
	//selectedRange: [that.vm.renderData.checkIn, that.vm.renderData.checkOut]
	selectRange: true,
    // useTransform: /Android/.test(navigator.userAgent) ? false : true,
    selectSameDay: false,
    hideAfterSelected: true,
    maxRange: 30,
    maxRangeNote: '亲，往返机票只能预定30天',
    selectRangeNote: ['请选择出发日期', '请选择返程日期'],
    selectedRangeCallback: function(e){
    	console.log(e);
	}
    // navigateByHash: that.isHash
});
__calendar.render();
__calendar.show();

```


### mask 蒙层组件

- 蒙层组件
- 使用方法

```javascript

var mask = new Mask({
    fadeIn: true, //是否使用动画（渐隐渐现）
    content: '<div class="title">退改签规则</div>', //蒙层内部内容 内容自定义
    tapMaskCallback: function(){ // 点击黑蒙层的 回调函数
        console.log(111);
        mask.hide();
    },
    afterHide: function(){ //隐藏之后的 回调函数
        console.log('mask hide');
    }
});
mask.show();

```

### pad

- 面板组件
- 使用方法
- 可扩展多个方法 （暂时与mask分离）

```javascript

var pad = new Pad({
    height: '200px', //自定义高度 默认 300px
    fadeIn: true, //蒙层是否使用动画（渐隐渐现）
    title: '退改签规则', //title 选填
    content: '运价有效期内，起飞前2个小时前 (含) 收取商务舱全价票 (J) 的10%', //内容 自定义
    tapMaskCallback: function(){ // 点击黑蒙层的 回调函数
        console.log(111);
        pad.hide();
    },
    afterHide: function(){ //隐藏之后的 回调函数
        console.log('mask hide');
    }
});
pad.show();

```

### radioPad

- 单选面板组件 （底部滑出）
- 使用方法

```javascript

var radioGroup = new RadioPad({
    title: '证件类型', //标题
    customClass: 'aaa', //自定义class
    initIndex: 0, //初始化选中项
    radioList: [ //列表选项
        {
            key: 'idCard',
            value: '身份证'
        },
        {
            key: 'kepiaoCard',
            value: '客票号'
        },
        {
            key: 'huzhaoCard',
            value: '护照'
        },
        {
            key: 'otherCard',
            value: '其他证件'
        }
    ],
    onChange: function (e) { //点选选项之后的回调
        console.log(e);
        radioGroup.hide();
    }
});
radioGroup.show();

//无key值
var radioGroup = new RadioPad({
    title: '证件类型', //标题
    customClass: 'aaa', //自定义class
    initIndex: 0, //初始化选中项
    radioList: [ //列表选项
        '身份证',
        '客票号',
        '护照',
        '其他证件'
    ],
    onChange: function (e) { //点选选项之后的回调
        console.log(e);
        radioGroup.hide();
    }
});
radioGroup.show();

```


### util

- 工具方法集合
- 详见代码注释
- 常用

```javascript

Util.locStorage.set
Util.locStorage.get

Util.getAbsoultePath('./abc');

```














