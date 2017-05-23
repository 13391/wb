/**
 * 用户公司绘制模块
 */

define(function () {
    function draw() {
        $.ajax({
            url: 'api/getUserCompany.json',
            success: render
        });
    }

    function render(data) {
        data = JSON.parse(data);

        var width = 800;
        var height = 400;

        var svg = d3.select('.chart-company .svg-container')
            .append('svg')
            .attr('width', width)
            .attr('height', height);

        var padding = {
            left: 30,
            right: 30,
            top: 20,
            bottom: 20
        };

        var dataset = [];
        var companys = []
        data.groups.forEach(function (item) {
            dataset.push(item.count);
            companys.push(item.company);
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
                return d + 1 + ' [' + companys[i] + ']';
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
                return yScale(d);
            })
            .attr('dx', function () {
                return (xScale.rangeBand() - rectPadding) / 2;
            })
            .attr('dy', function (d) {
                return 20;
            })
            .text(function (d) {
                return d;
            });

        svg.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(' + padding.left + ',' + (height - padding.bottom) + ')')
            .call(xAxis);

        svg.append('g')
            .attr('class', 'axis')
            .attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
            .call(yAxis);

        $('.chart-company').append(template('userCompany', data));
    }

    return {
        draw: draw
    };
});