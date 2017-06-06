/**
 * 用户地址分析
 */

define(function () {
    'use strict';

    function draw() {
        $.ajax({
            url: 'api/getUserAddress.json',
            success: render
        });
    }

    function render(data) {
        data = JSON.parse(data);
        //画布大小
        var width = 800;
        var height = 400;

        var svg = d3.select('.chart-address')
            .append('svg')
            .attr('width', width)
            .attr('height', height);

        //画布周边的空白
        var padding = {
            left: 30,
            right: 30,
            top: 20,
            bottom: 50
        };

        var dataset = [];
        data.forEach(function(i) {
            dataset.push(i.count);
        });

        var xScale = d3.scale.ordinal()
            .domain(d3.range(dataset.length))
            .rangeRoundBands([0, width - padding.left - padding.right]); // 指定输出范围为连续区间，区间段的起点均为整数

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

        var rectPadding = 4;

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

        //添加文字元素
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
                return yScale(d) - 5;
            })
            .attr('dx', function () {
                return (xScale.rangeBand() - rectPadding) / 2;
            })
            .text(function (d) {
                return d;
            });

        svg.selectAll('.province-text')
            .data(data)
            .enter()
            .append('text')
            .attr({
                class: 'province-text',
                transform: 'translate(' + padding.left + ', 0)',
                x: function(d, i) {
                    return (xScale(i) + 2) + ' ' + (xScale(i) +10) + ' ' + (xScale(i) + 10);
                },
                y: '378 388 398',
                dx: function () {
                    return (xScale.rangeBand() - rectPadding) / 2;
                },
            })
            .text(function(d) {
                return d.province;
            });

        svg.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(' + padding.left + ',' + (height - padding.bottom) + ')')
            .call(xAxis);

        $('.chart-address').append(template('userAddress', {}));
    }

    return {
        draw: draw
    };
});