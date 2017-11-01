define(function(require, exports, module) {
    var ToastClass = function() {
        this.initialize.apply(this, arguments);

    };

    ToastClass.prototype = {

        initialize: function(msg, duration, extraclass) {

            this.msg = msg;
            this.duration = duration;
            this.extraclass = extraclass;

            this.toastInit();
            
        },

        toastInit: function(){
            
            var $toast = $('<div class="modal-toast ' + (this.extraclass || '') + '"><p>' + this.msg + '</p></div>').appendTo(document.body);
            var t1 = setTimeout(function() {
                $toast.addClass('modal-in');
                clearTimeout(t1);
            }, 100);

            var t2 = setTimeout(function() {

                $toast.removeClass('modal-in').addClass('modal-out');
                setTimeout(function(){
                    $toast.remove();
                }, 500);
                clearTimeout(t2);

            }, this.duration || 2000);
        }
    };

    module.exports = function(msg, duration, extraclass){
        return new ToastClass(msg, duration, extraclass);
    }

});



