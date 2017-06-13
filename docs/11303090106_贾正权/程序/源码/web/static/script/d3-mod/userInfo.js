/**
 * Created by Jiavan on 2017/5/23.
 */
define(function () {

    function render() {
        $.ajax({
            url: '/api/getUserInfo.json',
            success: function (res) {
                res = JSON.parse(res);
                $('body').css('background-image', 'url("' + res.userInfo.cover_image_phone + '")');
                $('.chart-user .content').html(template('userInfo', res.userInfo));
            }
        });
    }

    function renderCreated(res, interpolation) {
        var width = 700, height = 400;
        var padding = {top: 50, right: 50, bottom: 30, left: 50};
        $('.chart-user .created .svg-container').empty();
        var svg = d3.select('.chart-user .created .svg-container').append('svg').attr({
            width: width,
            height: height
        });
        var main = svg.append('g')
            .attr('transform', "translate(" + padding.top + ',' + padding.left + ')');

        var dataset = res.createdData.map(function (item, idx) {
            return {x: idx, y: item};
        });

        var xScale = d3.scale.linear()
            .domain([0, 23])
            .range([0, width - padding.left - padding.right]);
        var yScale = d3.scale.linear()
            .domain([0, d3.max(res.createdData)])
            .range([height - padding.top - padding.bottom, 0]);
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient('bottom')
            .ticks(24);
        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient('left')
            .ticks(d3.max(res.createdData));

        main.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(0,' + (height - padding.top - padding.bottom) + ')')
            .call(xAxis);
        main.append('g')
            .attr('class', 'axis')
            .call(yAxis);

        var line = d3.svg.line()
            .x(function(d) {
                return xScale(d.x)
            })
            .y(function(d) {
                return yScale(d.y);
            })
            .interpolate(interpolation || 'monotone');

        main.append('path')
            .attr('class', 'line')
            .attr('d', line(dataset));

        var colors = d3.scale.category20b();
        main.selectAll('circle')
            .data(dataset)
            .enter()
            .append('circle')
            .attr('cx', function(d) {
                return xScale(d.x);
            })
            .attr('cy', function(d) {
                return yScale(d.y);
            })
            .attr('r', 4)
            .attr('fill', function(d, i) {
                return colors(i);
            });
    }

    function renderSource(res) {
        data = res.sourceData;

        var width = 700;
        var height = 400;

        var svg = d3.select('.chart-user .source .svg-container')
            .append('svg')
            .attr('width', width)
            .attr('height', height);

        var padding = {top: 50, right: 50, bottom: 30, left: 50};
        var dataset = [];
        var sources = [];
        data.forEach(function (item) {
            dataset.push(item.count);
            sources.push(item.source);
        });

        var xScale = d3.scale.ordinal()
            .domain(d3.range(dataset.length))
            .rangeRoundBands([0, width - padding.left - padding.right]);

        var yScale = d3.scale.linear()
            .domain([0, d3.max(dataset)])
            .range([height - padding.top - padding.bottom, 0]);

        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient('bottom')
            .tickValues(d3.range(dataset.length))
            .tickFormat(function(d, i) {
                return d + 1;
            });
        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient('left');

        var rectPadding = 10;

        var colors = d3.scale.category10();
        var rects = svg.selectAll('.rect')
            .data(dataset)
            .enter()
            .append('rect')
            .attr('class', 'rect')
            .attr('fill', function (d, i) {
                return colors(i);
            })
            .attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
            .attr('x', function (d, i) {
                return xScale(i) + rectPadding / 2;
            })
            .attr('y', function (d) {
                return yScale(d);
            })
            .attr('width', xScale.rangeBand() - rectPadding)
            .attr('height', function (d) {
                return height - padding.top - padding.bottom - yScale(d);
            });

        var texts = svg.selectAll('.rect-text')
            .data(dataset)
            .enter()
            .append('text')
            .attr('class', 'rect-text')
            .attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
            .attr('x', function (d, i) {
                return xScale(i) + rectPadding / 2;
            })
            .attr('y', function (d) {
                return yScale(d) - 25;
            })
            .attr('dx', function () {
                return (xScale.rangeBand() - rectPadding) / 2;
            })
            .attr('dy', function (d) {
                return 20;
            })
            .text(function (d, i) {
                return sources[i];
            });

        svg.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(' + padding.left + ',' + (height - padding.bottom) + ')')
            .call(xAxis);

        svg.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
            .call(yAxis);

        // $('.chart-company').append(template('userCompany', data));
    }

    var document = window.document;
    $(document).on('click', '.search-info', function () {
        var input = $('.chart-user input').val();
        $.ajax({
            url: '/api/getUserBySearch.json',
            data: {
                username: input,
                type: 'userinfo'
            },
            success: function (res) {
                if (Object.keys(res).length) {
                    res = JSON.parse(res);
                    $('body').css('background-image', 'url("' + res.userInfo.cover_image_phone + '")');
                    $('.chart-user .content').html(template('userInfo', res.userInfo));
                } else {
                    window.alert('微博用户不存在!');
                }
            }
        });
    });

    $(document).on('click', '.search-portrait', function () {
        var input = $('.chart-user input').val();
        $.ajax({
            url: '/api/getUserBySearch.json',
            data: {
                username: input || '_WEIXUEYAN' || 'xcatliu',
                type: 'portrait'
            },
            success: function (res) {
                res = JSON.parse(res);
                if (Object.keys(res).length) {
                    var keywords = res.portrait[0].keywords.split(',');
                    var colors = d3.scale.category20();
                    res.keywords = keywords.map(function (item, idx) {
                        if (item === '链接') {
                            return {};
                        }
                        return {
                            value: item,
                            color: colors(idx),
                            size: parseInt(Math.random()*40 + 20),
                            angle: parseInt(-Math.random()*233)
                        };
                    });
                    res.introduction = res.portrait[0].introduction;

                    $('.chart-user .content').html(template('userPortrait', res));
                    renderCreated(res);
                    $(document).on('click', '.chart-user .interpolation', function () {
                        renderCreated(res, $(this).text());
                    });

                    renderSource(res);
                } else {
                    window.alert('爬虫未抓取该用户微博数据!');
                }
            }
        });
    });

    return {
        render: render
    };
});