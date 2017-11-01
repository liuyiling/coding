define(function(require, exports, module) {
    var _body = $('body');
    var ACTIVE = 'active';

    var CheckBoxClass = function() {
        this.initialize.apply(this, arguments);

    };

    CheckBoxClass.prototype = {

        initialize: function(params) {

            this.params = params;

            if(!params.checkboxList || !params.checkboxList.length) {
                console.error('需要传递checkboxList字段')
            }

            this.domInit();
            this.statusInit();
            this.bindEvent();

        },

        domInit: function(){

            var listPanel = $('<div class="checkbox-list"></div>');
            if(this.params.customClass){
                listPanel.addClass(this.params.customClass);
            }

            var htmlStr = '';
            var checkboxList = this.params.checkboxList;
            var initIndex = this.params.initIndex;
            var isInitIndex = initIndex && initIndex.length;
            var noKey = !checkboxList[0].key;

            $.each(checkboxList, function (index, item) {

                if(!noKey){
                    htmlStr += '<div class="checkbox-item ' + (isInitIndex && initIndex.indexOf(index) != -1 ? 'active' : '') + '" data-key="' + item.key + '" data-value="' + item.value + '"> <span>' + item.value + '</span> <i class="checkbox-icon"></i> </div>';
                } else {
                    htmlStr += '<div class="checkbox-item ' + (isInitIndex && initIndex.indexOf(index) != -1 ? 'active' : '') + '" data-value="' + item + '"> <span>' + item + '</span> <i class="checkbox-icon"></i> </div>';
                }

            });

            listPanel.append(htmlStr);
            this.listPanel = listPanel;
            this.noKey = noKey;

        },

        statusInit: function () {
            this.status = this.params.initIndex || [];
        },

        changeStatus: function (index) {
            var indexOf = this.status.indexOf(index);
            if(indexOf != -1){
                this.status.splice(indexOf, 1);
            } else {
                this.status.push(index);
            }

            this.updateDom();
        },

        updateDom: function () {
            var child = this.listPanel.children();
            child.removeClass(ACTIVE);
            $.each(this.status, function (index, item) {
                child.eq(item).addClass(ACTIVE);
            });
        },

        getResult: function () {
            var result = [];
            var checkboxList = this.params.checkboxList;

            $.each(this.status, function (index, item) {
                result.push(checkboxList[item]);
            });
            return result;

        },

        bindEvent: function(){
            var child = this.listPanel.children();
            var onChange = this.params.onChange;
            var self = this;
            child.on('click', function (e) {
                var that = $(this);
                self.changeStatus(that.index());

                onChange && onChange(self.getResult());
            });
        },

        getNode: function () {
            return this.listPanel;
        }

    };

    module.exports = CheckBoxClass;

});



