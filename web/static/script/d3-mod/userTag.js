/**
 * 用户标签
 */

define(function () {
    function draw() {
        $.ajax({
            url: 'api/getUserTag.json',
            success: render
        });
    }

    function render(data) {
        data = JSON.parse(data);

        var width = 700;
        var height = 400;

        //在 body 里添加一个 SVG 画布
        var svg = d3.select('.chart-tag .svg-container')
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

        //定义一个数组
        var dataset = [];
        data.groups.forEach(function(i) {
            dataset.push(i.count);
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
                return (d + 1) + ' [' + data.groups[i].tags + ']';
            });

        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient('left');

        var rectPadding = 10;

        var colors = d3.scale.category10();
        data.groups.forEach(function (item, idx) {
            item.color = colors(idx);
        });
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
        svg.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(' + padding.left + ',' + (height - padding.bottom) + ')')
            .call(xAxis);



        $('.chart-tag').append(template('userTag', data));
    }

    return {
        draw: draw
    };
});