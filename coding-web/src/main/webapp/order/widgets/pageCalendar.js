define(function(require, exports, module) {
    var DOC = document;

    function fixNum(num) {
        return (num < 10 ? '0' : '') + num;
    }

      //如果在日历打开页面刷新页面，那么后退到打开日历的当前页面
    function checkPageCalendarHash() {
        if (location.hash.match(/Page_Calendar_Hash_/i)) {
            history.back();
            window.scrollTo(0, 0);
        }
    }

    function substitute(tpl, data){
        return tpl.replace(/\{(\w+)\}/g,function(a, b){
            return data[b];
        });
    }


    var win = $(window),
        bodyElem = bodyElem || $(document.body),
        HASH_ID = 0,
        MONTH_DAYS = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],

        CALENDAR_HEADER = '<div class="m-pcalendar-header"><span class="m-pcalendar-cancel">{cancelNote}</span><strong>{title}</strong><span class="m-pcalendar-confirm">{confirmNote}</span></div>',
        CALENDAR_WEEKDAY = '<table class="m-pcalendar-weekday">' +
            '<thead>' +
            '<tr>' +
            '<th>日</th>' +
            '<th>一</th>' +
            '<th>二</th>' +
            '<th>三</th>' +
            '<th>四</th>' +
            '<th>五</th>' +
            '<th>六</th>' +
            '</tr>' +
            '</thead>' +
            '</table>',
        CALENDAR_EMPTY = '<td class="m-pcalendar-empty"></td>',
        CALENDAR_CELL = '<td class="{cls}" data-date="{date}" data-note="{note}" data-day="{day}">' +
            '<div class="m-pcalendar-cell">' +
            '<em class="m-pcalendar-note">{note}</em>' +
            '<strong class="m-pcalendar-day">{text}</strong>' +
            '<span class="m-pcalendar-ext">{ext}</span>' +
            '</div>' +
            '</td>',
        CALENDAR_MONTH = '<div class="m-pcalendar-panel">' +
            '<div class="m-pcalendar-month">{chead}</div>' +
            '<div class="m-pcalendar-grid">' +
            '<table class="m-pcalendar-table">' +
            '<tbody>{tbody}</tbody>' +
            '</table>' +
            '</div>' +
            '</div>';

    //国际节日
    var GLOBAL_HOLIDAY = {
        '01-01': '元旦',
        '02-14': '情人',
        '05-01': '劳动',
        '06-01': '儿童',
        '10-01': '国庆',
        '12-25': '圣诞'
    };

    //传统节日
    var TRADITIONAL_HOLIDAY = {
        '除夕': ['2013-02-09', '2014-01-30', '2015-02-18', '2016-02-07', '2017-01-27', '2018-02-15', '2019-02-04', '2020-01-24'],
        '春节': ['2013-02-10', '2014-01-31', '2015-02-19', '2016-02-08', '2017-01-28', '2018-02-16', '2019-02-05', '2020-01-25'],
        '元宵': ['2013-02-24', '2014-02-14', '2015-03-05', '2016-02-22', '2017-02-11', '2018-03-02', '2019-02-19', '2020-02-08'],
        '清明': ['2013-04-04', '2014-04-05', '2015-04-05', '2016-04-04', '2017-04-04', '2018-04-05', '2019-04-05', '2020-04-04'],
        '端午': ['2013-06-12', '2014-06-02', '2015-06-20', '2016-06-09', '2017-05-30', '2018-06-18', '2019-06-07', '2020-06-25'],
        '中秋': ['2013-09-19', '2014-09-08', '2015-09-27', '2016-09-15', '2017-10-04', '2018-09-24', '2019-09-13', '2020-10-01'],
        '重阳': ['2013-10-13', '2014-10-02', '2015-10-21', '2016-10-09', '2017-10-28', '2018-10-17', '2019-10-07', '2020-10-25']
    };

    var CALENDAR_TIPS = '<div class="m-pcalendar-tips J_pcalendar_tips_go">' +
        '   <div class="m-pcalendar-tips-content">{dateGoNote}</div>' +
        '</div>' +
        '<div class="m-pcalendar-tips J_pcalendar_tips_back">' +
        '   <div class="m-pcalendar-tips-content">{dateBackNote}</div>' +
        '</div>';

    var CALENDAR_ERROR = '<div class="m-pcalendar-error-tips">{maxRangeNote}</div>';

    var HOLIDAY_TEMP = {};

    $.each(TRADITIONAL_HOLIDAY, function(key, val) {
        $.each(val, function(index, item) {
            HOLIDAY_TEMP[item] = key;
        });
    });

    TRADITIONAL_HOLIDAY = HOLIDAY_TEMP;
    

    var PageCalendar = function() {
        this.initialize.apply(this, arguments);
    };

    PageCalendar.prototype = {

        /**
         * 初始化组件
         * @method initializer
         */
        initialize: function(params) {
            
            this._domCache = {};
            this.dealAttrs(params);

            this._hashId = 'Page_Calendar_Hash_' + (params.hashId || ++HASH_ID);
            this._showNote = params.showNote;
            this._dateRange = params.dateRange;
            // this._showHeader = params.showHeader;
            this._confirmable = params.confirmable;
            // this._navTitleSetter = this.get('navTitleSetter');
            this._selectRange = params.selectRange;
            this._selectRangeNote = params.selectRangeNote;
            this._selectSameDay = params.selectSameDay;
            this._navigateByHash = params.navigateByHash;
            // this._setAliTripBackButton = this.get('setAliTripBackButton');
            this._fireSelectDelay = params.fireSelectDelay;
            this._hideAfterSelected = params.hideAfterSelected;
            this._maxRange = params.maxRange;
            this._maxRangeNote = params.maxRangeNote;

            //设置今天参数
            this._today = new Date();
            this._today.setHours(0);
            this._today.setMinutes(0);
            this._today.setSeconds(0);
            this._today.setMilliseconds(0);
            this.ATTRS.today = this._today;

        },

        dealAttrs: function(params){
            this.ATTRS = {
                parentNode: (params.parentNode && params.parentNode) || bodyElem,
                title: '选择日期',
                cancelNote: '取消',
                confirmNote: '完成',
                confirmable: false,
                showNote: false,
                rendered: false,
                today: new Date(),
                visible: false,
                selectedDate: '',
                selectedRange: [],
                selectRangeNote: ['请选开始日期', '请选结束日期'],
                // selectedItem: false, //todo 
                selectedNote: false, //todo
                selectSameDay: true,
                fireSelectDelay: false, //todo
                hideAfterSelected: true,
                resetLastSelected: true,
                navigateByHash: true,
                maxRangeNote: '超出最大可选范围'
            };

            for(var key in params){
                this.ATTRS[key] = params[key];
            }

            // this.ATTRS.selectedItem = this.getItemByDate(this.ATTRS.selectedDate);
            this.ATTRS.selectedNote = this.ATTRS.selectedNote || this.ATTRS.selectRange ? ['去程', '返程', '往返'] : '出发';
            this.ATTRS.fireSelectDelay = this.ATTRS.confirmable ? -1 : ('ontouchstart' in window) ? 25 : 100;
            
        },

        /**
         * 渲染默认回调
         * @method _defRenderFn
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _defRenderFn: function(e){
            this._parentNode = e.parentNode || this.ATTRS.parentNode;
            this._render();
            this.ATTRS.rendered = true;
        },

        /**
         * 渲染日历
         * @method _render
         * @protected
         */
        _render: function() {
            this.renderUI();
            this.bindUI();
            this.syncUI();
        },

        /**
         * 渲染组件
         * @method render
         * @chainable
         */
        render: function(parentNode) {

            if (!this.ATTRS.rendered) {
                this._defRenderFn({
                    parentNode: parentNode
                });
            }

            return this;
        },

        /**
         * 渲染UI
         * @method renderUI
         */
        renderUI: function() {
            var node = this.ATTRS.srcNode,
                id = this.ATTRS.id;

            if (!node) {
                node = $('<div></div>');
                this.ATTRS.srcNode = node;
            }

            this._srcNode = node;

            if (this._parentNode) {
                this._parentNode.append(node);
            }

            id && node.attr('id', id);
            node.addClass('m-pcalendar');
            node.addClass('m-pcalendar-hidden');
            this._showNote && node.addClass('m-pcalendar-shownote');
            this._showHeader && node.addClass('m-pcalendar-showheader');
            this._confirmable && node.addClass('m-pcalendar-confirmable');

            var range = this._dateRange,
                start = new Date(range[0].replace(/-/g, '/')),
                end = new Date(range[1].replace(/-/g, '/')),
                startYear = start.getFullYear(),
                startMonth = start.getMonth() + 1,
                startDate = start.getDate(),
                endYear = end.getFullYear(),
                endMonth = end.getMonth() + 1,
                endDate = end.getDate(),
                i = 0,
                l = (endYear - startYear) * 12 + endMonth - startMonth + 1,
                y = startYear,
                n = startMonth,
                header = substitute(CALENDAR_HEADER, {
                    title: this.ATTRS.title,
                    cancelNote: this.ATTRS.cancelNote,
                    confirmNote: this.ATTRS.confirmNote
                }),
                tips = '',
                html = '';

            this._checkLeapYear();

            node.empty();

            for (; i < l; i++) {
                if (n > 12) {
                    n = 1;
                    y++;
                }
                html += this._renderPanel(y, n, i === 0 ? startDate : false, (i === l - 1) ? endDate : false);
                n++;
            }

            html = header + CALENDAR_WEEKDAY + '<div class="m-pcalendar-body">' + html + '</div>';

            //为范围选择增加提示
            if (this._selectRange) {
                tips = substitute(CALENDAR_TIPS, {
                    dateGoNote: this._selectRangeNote[0],
                    dateBackNote: this._selectRangeNote[1]
                });

                tips += substitute(CALENDAR_ERROR, {
                    maxRangeNote: this._maxRangeNote
                });

                html += tips;
            }

            node.append(html);

            this._weekdayNode = node.find('.m-pcalendar-weekday');
            this._headerNode = node.find('.m-pcalendar-header');

            if (this._selectRange) {
                this._tipsGoBackNode = node.find('.J_pcalendar_tips_back,.J_pcalendar_tips_go');
                this._tipsGoNode = node.find('.J_pcalendar_tips_go');
                this._tipsBackNode = node.find('.J_pcalendar_tips_back');
                this._tipsErrorNode = node.find('.m-pcalendar-error-tips');
            }
        },

         /**
         * 渲染月份
         * @method _renderPanel
         * @param {String} year 年
         * @param {String} month 月
         * @param {String} start 开始日期
         * @param {String} end 结束日期
         * @protected
         */
        _renderPanel: function(year, month, start, end) {
            this._checkLeapYear(year);
            start = start || 1;
            end = end || MONTH_DAYS[month - 1];

            var node = this._srcNode,
                padding = this._getPadding(year, month, 7),
                num = MONTH_DAYS[month - 1] + padding,
                rows = Math.ceil(num / 7),
                remain = num % 7,
                html = '',
                row, cell, panel,
                day, i, j, date;

            for (i = 1; i <= rows; i++) {

                row = '';

                for (j = 1; j <= 7; j++) {
                    day = cell = date = '';

                    //前后空格
                    if ((i === 1 && j <= padding) || (remain && i === rows && j > remain)) {
                        cell = CALENDAR_EMPTY;
                    } else {
                        day = (i - 1) * 7 + j - padding;
                        date = year + '-' + fixNum(month) + '-' + fixNum(day);
                        cell = this._renderCell(date, year, month, day);
                    }

                    row += cell;
                }

                html += ('<tr class="m-pcalendar-row">' + row + '</tr>');
            }

            panel = substitute(CALENDAR_MONTH, {
                chead: year + '-' + fixNum(month),
                tbody: html
            });

            return panel;
        },

        /**
         * 渲染日期表格
         * @method _renderCell
         * @param {String} date 日期
         * @param {String} y 年
         * @param {String} m 月
         * @param {String} d 日
         * @return {String} cell 表格HTML
         */
        _renderCell: function(date, y, m, d) {
            var cls = ['m-pcalendar-date', 'm-pcalendar-date-' + date],
                isInRange = this.isInRange(date),
                noteFilter = this.ATTRS.noteFilter,
                classFilter = this.ATTRS.classFilter,
                disabledFilter = this.ATTRS.disabledFilter,
                extFilter = this.ATTRS.extFilter,
                disabled = false,
                global = fixNum(m) + '-' + fixNum(d),
                note = '',
                ext = '',
                ret, cell;

            //国际节日    
            if (GLOBAL_HOLIDAY[global]) {
                note = GLOBAL_HOLIDAY[global];
                cls.push('m-pcalendar-holiday');
            }

            //传统节日    
            if (TRADITIONAL_HOLIDAY[date]) {
                note = TRADITIONAL_HOLIDAY[date];
                !GLOBAL_HOLIDAY[global] && cls.push('m-pcalendar-holiday');
            }

            //周末
            if (this.isWeekend(date)) {
                cls.push('m-pcalendar-weekend');
            }

            //今天
            if (this.isToday(date)) {
                cls.push('m-pcalendar-today');
                note = '今天';
            }

            //不在日期范围内    
            if (!isInRange) {
                disabled = true;
            }

            //文案过滤器
            if (noteFilter) {
                ret = noteFilter.call(this, date, y, m, d, note);
                if (typeof ret !== 'undefined') {
                    note = ret;
                }
            }

            //禁止点击过滤器
            if (disabledFilter) {
                ret = disabledFilter.call(this, date, y, m, d, disabled);
                if (typeof ret !== 'undefined') {
                    disabled = ret;
                }
            }

            if (disabled) {
                cls.push('m-pcalendar-disabled');
            }

            //class过滤器
            if (classFilter) {
                ret = classFilter.call(this, date, y, m, d, cls);
                if (typeof ret !== 'undefined') {
                    cls = ret;
                } else if (typeof ret === 'string') {
                    cls.push(ret);
                }
            }

            //扩展过滤器
            if (extFilter) {
                ret = extFilter.call(this, date, y, m, d, ext);
                if (typeof ret !== 'undefined') {
                    ext = ret;
                }
            }

            if (!ext && disabled && isInRange) {
                ext = '不可选';
            }

            cell = substitute(CALENDAR_CELL, {
                cls: cls.join(' '),
                note: note,
                date: date,
                ext: ext,
                disabled: disabled,
                year: y,
                month: m,
                day: d,
                text: this._showNote && note || d
            });

            return cell;
        },


        /**
         * 绑定UI事件
         * @method bindUI
         */
        bindUI: function() {
            var self = this;
            this._srcNode.on('click', '.m-pcalendar-date', function(e) {
                var target = $(e.currentTarget);
                self.select(target.attr('data-date'), !self._confirmable);
            });
            this._headerNode.on('click', '.m-pcalendar-cancel', this._onCancelClick, this);
            this._headerNode.on('click', '.m-pcalendar-confirm', this._onConfirmClick, this);

            win.on('hashchange', function(e){
                this._clearAnim();
                this._onHashChange(e);
            }, this);

        },

        moreThanMaxRange: function(){
            this._showErrorToast();
        },

        /**
         * 选中日期
         * @method select
         * @param {String|Array} date 日期
         * @param {Boolean} confirm 是否强制完成
         * @chainable
         */
        select: function(date, confirm) {
            var self = this;

            if (date instanceof Array) {

                if (date.length > 2) {
                    date.length = 2;
                }

                if (date.length == 1) {
                    this.unselect();
                }

                $.each(date, function(index, d) {
                    self.select(d, confirm);
                });
                return this;
            };

            var item, facade;

            if (date &&
                !this._firingSelectEvent &&
                (item = this.getItemByDate(date)) &&
                !item.hasClass('m-pcalendar-disabled')) {

                facade = {
                    date: date,
                    item: item,
                    note: item.attr('data-note'),
                    prevDate: this.ATTRS.selectedDate
                };

                if(this._maxRange && !this.isSelected() &&
                    this._maxRange < this._rangeDays(facade.prevDate, facade.date)){
                    this.moreThanMaxRange();
                    return this;
                }

                this._dealSelectDate(facade);

                if (this._selectRange) {
                    this._dealSelectRange(facade);
                }

                confirm && this.confirm();
            }

            return this;
        },


        /**
         * 取消选中日期
         * @method unselect
         * @chainable
         */
        unselect: function() {
            this._resetSelectedItems();
            this.ATTRS.selectedDate = '';
            this.ATTRS.selectedRange = [];
            return this;
        },

        /**
         * 通过选中日期
         * @method getSelected
         * @return {Array|String}
         */
        getSelected: function() {
            return this.ATTRS[this._selectRange ? 'selectedRange' : 'selectedDate'];
        },

        /**
         * 处理选择区间情况
         * @method _dealSelectRange
         * @param {Object} facade 选中日期对象
         * @protected
         */
        _dealSelectRange: function(facade) {
            var prevDate = facade.prevDate,
                newDate = facade.date,
                range;

            if (this._startItem && !this._endItem &&
                ((this.getTime(newDate) > this.getTime(prevDate)) ||
                    (this._selectSameDay && this.getTime(newDate) == this.getTime(prevDate)))) {

                //触发选择区间结束事件
                this._defSelectRangeEndFn(facade);

                //触发选择区间事件
                range = this.ATTRS.selectedRange;
                
                this._defSelectRangeFn({
                    range: range,
                    items: this._getRangeItems(range)
                });
            } else {

                //触发选择区间开始事件
                this._defSelectRangeStartFn(facade);
            }
        },

        /**
         * 处理单选情况
         * @method _dealSelectDate
         * @param {Object} facade 选中日期对象
         * @protected
         */
        _dealSelectDate: function(facade) {
            this._defSelectDateFn(facade);
        },

        /**
         * 选择开始默认回调
         * @method _defSelectRangeStartFn
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _defSelectRangeStartFn: function(e) {
            var item = e.item,
                selectedNote = this.ATTRS.selectedNote[0];

            this._resetSelectedItems();

            this._startItem = item;

            item.addClass('m-pcalendar-selected');
            item.addClass('m-pcalendar-selected-start');
            selectedNote && this._setItemNote(item, selectedNote);
            this._showNote && this._toggleSelectedDayAndNote(item, true);

            this.ATTRS.selectedRange = [e.date];

            this._showDateTips();
        },

        /**
         * 选择区间默认回调
         * @method _defSelectRangeFn
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _defSelectRangeFn: function(e) {
            var items = e.items;

            this._rangeItems = items.range;
            items.range.addClass('m-pcalendar-selected-range');
        },

        /**
         * 选择默认回调
         * @method _defSelectFn
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _defSelectDateFn: function(e) {

            if (!this._selectRange) {
                var prevItem = this.getItemByDate(this.ATTRS.selectedDate),
                    newItem = this.getItemByDate(e.date),
                    selectedDate = this.ATTRS.selectedDate,
                    selectedNote = this.ATTRS.selectedNote;

                if (prevItem) {
                    prevItem.removeClass('m-pcalendar-selected');
                    this._resetItemNote(prevItem);
                    this._showNote && this._toggleSelectedDayAndNote(prevItem, false);
                }

                if (newItem) {
                    newItem.addClass('m-pcalendar-selected');
                    selectedNote && this._setItemNote(newItem, selectedNote);
                    this._showNote && this._toggleSelectedDayAndNote(newItem, true);
                }

                this._selectedItem = newItem;
            }

            this.ATTRS.selectedDate = e.date;
        },

        /**
         * 选中事件默认回调
         * @method _defSelectFn
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _defSelectFn: function() {
            this._lastSelected = this.getSelected();

            if (this._hideAfterSelected) {
                this.hide();
            }
        },

        /**
         * 选择结束默认回调
         * @method _defSelectRangeEndFn
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _defSelectRangeEndFn: function(e) {
            var selectedRange = this.ATTRS.selectedRange,
                selectedNote = this.ATTRS.selectedNote,
                item = e.item;

            if (selectedRange[0] === e.date) {
                selectedNote = selectedNote[2] || selectedNote[1];
            } else {
                selectedNote = selectedNote[1];
            }

            this._endItem = item;

            item.addClass('m-pcalendar-selected');
            item.addClass('m-pcalendar-selected-end');
            selectedNote && this._setItemNote(item, selectedNote);
            this._showNote && this._toggleSelectedDayAndNote(item, true);

            this.ATTRS.selectedRange = [selectedRange[0], e.date];

            this._showDateTips();
        },

        /**
         * 更新UI状态
         * @method syncUI
         */
        syncUI: function() {
            this._syncVisibility();
            this._syncHash();
            this._syncViewPortHeight();
            this._syncDefSelected();
            this._syncLastSelected(true);

            this.scrollToSelectedMonth();
        },

        /**
         * 更新默认选中状态
         * @method _syncDefSelected
         * @protected
         */
        _syncDefSelected: function() {
            this.select(this.getSelected());
        },

        /**
         * 展示开始、结束日期tips
         * @methos _showDateTips
         */
        _showDateTips: function () {
            if (!this._selectRange) {
                return;
            }

            var that = this;
            var IN = 'anim-wipeIn',
                OUT = 'anim-wipeOut';

            that._tipsGoBackNode.removeClass(IN + ' ' + OUT);

            var visible = that.ATTRS.visible;
            if (!visible) return;

            if (that.ATTRS.selectedRange.length == 1) {
                that._tipsGoNode.addClass(OUT);
                setTimeout(function () {
                    that._tipsBackNode.addClass(IN);
                }, 20);
            } else {
                that._tipsGoNode.addClass(IN);
            }
        },

        /**
         * 计算区间的间隔天数
         * @method _rangeDays
         * @protected
         * @example _rangeDays('2015-10-02', '2015-10-04') === 2
         */
        _rangeDays: function(start, end) {
            var start_arr = start.split("-");
            var end_arr = end.split("-");
            var startTime = new Date(start_arr[0], start_arr[1] - 1, start_arr[2]).getTime();
            var endTime = new Date(end_arr[0], end_arr[1] - 1, end_arr[2]).getTime();
            return (endTime - startTime) / (1000 * 3600 * 24);
        },

         /**
         * 重置节点文案
         * @method _resetItemNote
         * @param {Node|NodeList} 节点
         * @protected
         */
        _resetItemNote: function(item) {
            var self = this;

            item && item.each(function(index, i) {
                self._setItemNote($(i));
                self._showNote && self._toggleSelectedDayAndNote($(i), false);
            });
        },

        /**
         * 设置节点文案
         * @method _setItemNote
         * @param {Node|NodeList} 节点
         * @protected
         */
        _setItemNote: function(item, note) {
            item.find('.m-pcalendar-note').html(note || item.attr('data-note') || '');
        },

        /**
         * 切换选中节点的日期和文案
         * @method _toggleSelectedDayAndNote
         * @param {Node|NodeList} 节点
         */
        _toggleSelectedDayAndNote: function(item, selected) {
            item.find('.m-pcalendar-day').html(!selected && item.attr('data-note') || item.attr('data-day') || '');
        },

        /**
         * 是否已经选中
         * @method isSelected
         * @return {Boolean}
         */
        isSelected: function() {
            return this._selectRange ? this.ATTRS.selectedRange.length > 1 : !!this.ATTRS.selectedDate;
        },

        /**
         * 通过日期获取节点
         * @method getItemByDate
         * @param {String} date
         */
        getItemByDate: function(date) {
            var cache = this._domCache;
            return cache[date] || (cache[date] = this._srcNode.find('.m-pcalendar-date-' + date).eq(0));
        },

        /**
         * 重置选中区间节点状态
         * @method _resetSelectedItems
         * @protected
         */
        _resetSelectedItems: function() {

            if (this._selectedItem) {
                this._selectedItem.removeClass('m-pcalendar-selected');
                this._resetItemNote(this._selectedItem);
                delete this._selectedItem;
            }

            if (this._startItem) {
                this._startItem.removeClass('m-pcalendar-selected');
                this._startItem.removeClass('m-pcalendar-selected-start');
                this._resetItemNote(this._startItem);
                delete this._startItem;
            }

            if (this._endItem) {
                this._endItem.removeClass('m-pcalendar-selected');
                this._endItem.removeClass('m-pcalendar-selected-end');
                this._resetItemNote(this._endItem);
                delete this._endItem;
            }

            if (this._rangeItems) {
                this._rangeItems.removeClass('m-pcalendar-selected-range');
                this._resetItemNote(this._rangeItems);
                delete this._rangeItems;
            }

        },

        /**
         * 处理选中事件（统一selectDate与selectRange）
         * @method _dealSelect
         * @protected
         */
        _dealSelect: function() {
            var self = this,
                range = this.ATTRS.selectedRange,
                date = this.ATTRS.selectedDate,
                visible = this.ATTRS.visible,
                isSelected = this.isSelected(),
                fireSelect, facade, item;

            if (isSelected) {

                if (this._selectRange) {
                    facade = {
                        selected: range,
                        range: range,
                        items: this._getRangeItems(range)
                    };
                } else {
                    item = this.getItemByDate(date);
                    facade = {
                        selected: date,
                        date: date,
                        item: item,
                        note: item.attr('data-note')
                    };
                }

                var selectedRangeCallback = this.ATTRS.selectedRangeCallback;//选择结束的回调

                fireSelect = function() {
                    clearTimeout(self._firingSelectTimer);
                    delete self._firingSelectTimer;
                    delete self._firingSelectFn;
                    self._firingSelectEvent = false;
                    selectedRangeCallback && selectedRangeCallback(facade.selected);
                    self._defSelectFn(facade);
                };

                this._firingSelectEvent = true;
                this._firingSelectFn = fireSelect;

                //展示的时候才延时触发
                if (this._fireSelectDelay > -1 && visible) {
                    this._firingSelectTimer = setTimeout(fireSelect, this._fireSelectDelay);
                } else {
                    fireSelect();
                }
            }
        },

        /**
         * 获取选中区间节点
         * @method _getRangeItems
         * @param {Array} range 区间数组
         * @return {Object} 开始/结束/中间节点
         * @protected
         */
        _getRangeItems: function(range) {
            var cache = this._domCache,
                rangeStr = range.join('/');

            if (cache[rangeStr]) {
                return cache[rangeStr];
            } else {
                var start = this.getItemByDate(range[0]),
                    end = this.getItemByDate(range[1]),
                    items = [],
                    next;

                if (start[0] != end[0]) {
                    next = this._getNextItem(start);
                    while (next && next[0] != end[0]) {
                        items.push(next[0]);
                        next = this._getNextItem(next);
                    }
                }

                items = $(items);

                cache[rangeStr] = {
                    start: start,
                    end: end,
                    range: items
                };
            }

            return cache[rangeStr];
        },


        /**
         * 获取下一个日期节点
         * @method _getNextItem
         * @param {Node} item 日期节点
         * @protected
         */
        _getNextItem: function(item) {
            var next = null,
                nextParent;

            if (item) {
                next = item.next('.m-pcalendar-date');
                if ((!next || !next.length) && (nextParent = item.parent().next() || item.parents('.m-pcalendar-panel').next())) {
                    next = nextParent.find('.m-pcalendar-date').eq(0);
                }
            }

            return next;
        },


        /**
         * 点击日历节点回调
         * @method _onDateClick
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _onDateClick: function(e) {
            var target = $(e.currentTarget);
            this.select(target.attr('data-date'), !this._confirmable);
        },

        /**
         * 展示错误弱提示
         * @methos _showErrorToast
         */
        _showErrorToast: function () {
            var that = this;

            if (!this._tipsErrorNode) { return; }

            that._tipsErrorNode.removeClass('m-pcalendar-error-tips-anim');
            //不加定时器无效果
            setTimeout(function(){
                that._tipsErrorNode.addClass('m-pcalendar-error-tips-anim');
            }, 20);
        },

        /**
         * 页面hash变化回调
         * @method _onHashChange
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _onHashChange: function(e) {
            if (this._navigateByHash) {

                if (this._checkHashId(e.oldURL)) {
                    this.cancel();
                } else if (this._checkHashId(e.newURL)) {
                    this.show();
                }
            }
        },

        /**
         * 显隐性发生变化前回调
         * @method _beforeVisibleChange
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _beforeVisibleChange: function(e) {
            if (this._firingSelectFn) {
                this._firingSelectFn();
            }
        },

        /**
         * 显隐性发生变化回调
         * @method _afterVisibleChange
         * @param {EventFacade} e 事件对象
         * @protected
         */
        _afterVisibleChange: function(e) {
            this._syncVisibility(e);
            this._syncHash(e);
            this._syncViewPortHeight(e, true);
            this._syncLastSelected(e);
            this.scrollToSelectedMonth();
        },

        /**
         * 更新显隐性
         * @method _syncVisibility
         * @param {Boolean} visible 显隐性
         * @protected
         */
        _syncVisibility: function(visible) {
            visible = typeof visible === 'undefined' ? this.ATTRS.visible : visible;

            visible && (this._originPageYOffset = window.pageYOffset);
            this._originBodyHeight = visible ? bodyElem[0].style.height : '';
            this._srcNode.toggleClass('m-pcalendar-hidden', !visible);

            this._showDateTips();
        },

        /**
         * 同步hash值
         * @method _syncHash
         * @param {Boolean} visible 显隐性
         * @protected
         */
        _syncHash: function(visible) {
            if (this._navigateByHash) {

                visible = typeof visible === 'undefined' ? this.ATTRS.visible : visible;

                if (visible) {
                    location.hash = this._hashId;
                } else if (this._checkHashId(location.href)) {
                    history.back();
                }
            }
        },

        /**
         * 更新窗口高度
         * @method _syncViewPortHeight
         * @param {Boolean} visible 显隐性
         * @param {Boolean} change 是否显隐性发生了变化
         * @protected
         */
        _syncViewPortHeight: function(visible, change) {
            var visible = typeof visible === 'undefined' ? this.ATTRS.visible : visible,
                height = this._srcNode.offset().height + 'px';

            bodyElem.toggleClass('m-pcalendar-locked', visible);
            bodyElem[0].style.height = visible ? height : this._originBodyHeight;
            !visible && change && window.scrollTo(0, this._originPageYOffset || 0);
        },

        /**
         * 更新/恢复上次的选中值
         * @method _syncLastSelected
         * @param {Boolean} visible 显隐性
         * @protected
         */
        _syncLastSelected: function(visible) {
            if (visible) {
                this._lastSelected = this.getSelected();
            } else if (this.isSelectedChanged()) {
                this.reset();
            }
        },

        /**
         * 滚动至选中的月份处
         * @method scrollToSelectedMonth
         * @chainable
         */
        scrollToSelectedMonth: function() {

            if (this._selectRange) {
                this.scrollToMonth(this.ATTRS.selectedRange[0]);
            } else {
                this.scrollToMonth(this.ATTRS.selectedDate);
            }

            return this;
        },

        /**
         * 滚动至传入日期的月份处
         * @method scrollToMonth
         * @param {String} date 日期
         * @chainable
         */
        scrollToMonth: function(date) {
            var visible = this.ATTRS.visible,
                item, panel;

            if (visible) {
                if (date && (item = this.getItemByDate(date))) {
                    panel = item.parents('.m-pcalendar-panel');
                    window.scrollTo(0, panel.offset().top - this._headerNode.offset().height - this._weekdayNode.offset().height);
                } else {
                    window.scrollTo(0,0);
                }
            }

            return this;
        },

        /**
         * 日期是否改变选中（未保存）
         * @method isSelectedChanged
         * @return {Boolean}
         */
        isSelectedChanged: function() {
            var lastSelected = this._lastSelected,
                selected = this.getSelected();

            if (this._selectRange) {
                return (lastSelected[0] !== selected[0]) || (lastSelected[1] !== selected[1]);
            } else {
                return lastSelected !== selected;
            }
        },

        /**
         * 确认选择
         * @method confirm
         * @chainable
         */
        confirm: function() {
            this._dealSelect();
            return this;
        },

        /**
         * 取消选择
         * @method cancel
         * @chainable
         */
        cancel: function() {
            this.hide();
            // this.fire('cancel'); //todo
            return this;
        },


        /**
         * 展示
         * @method show
         * @chainable
         */
        show: function() {
            this._beforeVisibleChange();
            this.ATTRS.visible = true;
            this._afterVisibleChange(true);
            return this;
        },

        /**
         * 隐藏
         * @method hide
         * @chainable
         */
        hide: function() {
            this._beforeVisibleChange();
            this.ATTRS.visible = false;
            this._afterVisibleChange(false);
            return this;
        },

        /**
         * 恢复之前的值
         * @method reset
         * @chainable
         */
        reset: function() {
            this.unselect();
            this.select(this._lastSelected, false);
            return this;
        },


        /**
         * 检查是否是当前组件的hashId
         * @method _checkHashId
         * @param {String} URL 页面URL
         * @return {Boolean}
         * @protected
         */
        _checkHashId: function(URL) {
            if (URL &&
                URL.match(/#/) &&
                URL.replace(/^.*#/, '') === this._hashId) {

                return true;
            }

            return false;
        },


        /**
         * 关闭日历前去除动画，避免再次打开时重复执行
         * @private
         */
        _clearAnim: function(){
            if (!this._tipsErrorNode) { return; }
            this._tipsErrorNode.removeClass('m-pcalendar-error-tips-anim');
        },


        /**
         * 是否是今天
         * @method isToday
         * @param {String} date
         * @return {Boolean}
         */
        isToday: function(date) {
            return this._today.getTime() === this.getTime(date);
        },

        /**
         * 是否是周末
         * @method isWeekend
         * @param {String} date
         * @return {Boolean}
         */
        isWeekend: function(date) {
            var day = new Date(date.replace(/-/g, '/')).getDay();
            return day === 0 || day === 6;
        },

        /**
         * 是否在可选日期范围内
         * @method isInRange
         * @param {String} date
         * @return {Boolean}
         */
        isInRange: function(date) {
            var range = this._dateRange,
                start = this.getTime(range[0]),
                end = this.getTime(range[1]),
                date = this.getTime(date);

            return (start <= date && end >= date);
        },


        /**
         * 获取当前日期的毫秒数
         * @method getTime
         * @param {String} date
         * @return {Number}
         */
        getTime: function(date) {
            return new Date(date.replace(/-/g, '/')).getTime();
        },

        /**
         * 当月1号前面有多少空格
         * @method _getPadding
         * @protected
         */
        _getPadding: function(year, month, from) {
            var date = new Date(year + '/' + month + '/1'),
                day = date.getDay();

            return day;
        },


         /**
         * 检查是否是闰年
         * @method _checkLeapYear
         * @param {Number} y 年份
         * @protected
         */
        _checkLeapYear: function(y) {
            var y = y || this._today.getFullYear(),
                isLeapYear = false;

            if (y % 100) {
                isLeapYear = !(y % 4);
            } else {
                isLeapYear = !(y % 400);
            }

            if (isLeapYear) {
                MONTH_DAYS[1] = 29;
            } else {
                MONTH_DAYS[1] = 28;
            }
        }
    };

    module.exports = PageCalendar;
    
});