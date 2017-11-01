define(function(require, exports, module) {
    var ACTIVE = 'active';

    function Tab(TabContent, TabBar){

	    var J_TabContent = $(TabContent),
	        J_TabBar = $(TabBar);

	    J_TabBar.children().on('click', function(){
	        $(this).addClass(ACTIVE).siblings().removeClass(ACTIVE);

	        var index = $(this).index();

	        J_TabContent.children().eq(index).show().siblings().hide();
	    });

    }

    module.exports = Tab;
    
});