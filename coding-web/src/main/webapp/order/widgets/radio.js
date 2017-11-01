define(function(require, exports, module) {
    var _body = $('body');
    var ACTIVE = 'active';

    var RadioClass = function() {
        this.initialize.apply(this, arguments);

    };

    RadioClass.prototype = {

        initialize: function(params) {

            this.params = params;

            if(!params.radioList || !params.radioList.length) {
                console.error('需要传递radioList字段')
            }

            this.domInit();
            this.bindEvent();

        },

        domInit: function(){

            var listPanel = $('<div class="radio-list"></div>');
            if(this.params.customClass){
                listPanel.addClass(this.params.customClass);
            }

            var htmlStr = '';
            var radioList = this.params.radioList;
            var initIndex = this.params.initIndex;
            var isInitIndex = initIndex != -1;
            var noKey = !radioList[0].key;

            $.each(radioList, function (index, item) {

                if(item.key){
                    htmlStr += '<div class="radio-item ' + (isInitIndex && index == initIndex ? 'active' : '') + '" data-key="' + item.key + '" data-value="' + item.value + '"> <span>' + item.value + '</span> <i class="radio-icon"></i> </div>';
                } else {
                    htmlStr += '<div class="radio-item ' + (isInitIndex && index == initIndex ? 'active' : '') + '" data-value="' + item + '"> <span>' + item + '</span> <i class="radio-icon"></i> </div>';
                }

            });

            listPanel.append(htmlStr);
            this.listPanel = listPanel;

        },

        bindEvent: function(){
            var child = this.listPanel.children();
            var onChange = this.params.onChange;

            child.on('click', function (e) {
                var self = $(this);
                child.removeClass(ACTIVE);
                self.addClass(ACTIVE);

                onChange && onChange({
                    key: self.attr('data-key'),
                    value: self.attr('data-value')
                });
            });
        },

        getNode: function () {
            return this.listPanel;
        }

    };

    module.exports = RadioClass;

});



