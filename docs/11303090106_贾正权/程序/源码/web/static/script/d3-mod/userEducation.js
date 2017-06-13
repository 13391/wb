/**
 * 用户教育程度
 */

define(function () {

    function draw() {
        $.ajax({
            url: 'api/getUserEducation.json',
            success: render
        });
    }

    function render(data) {

        data = JSON.parse(data);
        var chartData = [];
        data.groups.forEach(function (i) {
            chartData.push(i.count);
        });

        var width = 300,
            height = 300,
            radius = Math.min(width, height) / 2 - 10;

        var color = d3.scale.category10();
        data.groups.forEach(function (item, idx) {
            item.color = color(idx);
        });

        var arc = d3.svg.arc()
            .outerRadius(radius);

        var pie = d3.layout.pie();

        var svg = d3.select('.chart-education .svg-container').append('svg')
            .datum(chartData)
            .attr('width', width)
            .attr('height', height)
            .append('g')
            .attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ')');

        var arcs = svg.selectAll('g.arc')
            .data(pie)
            .enter().append('g')
            .attr('class', 'arc');

        arcs.append('path')
            .attr('fill', function (d, i) {
                return color(i);
            })
            .transition()
            .ease('bounce')
            .duration(2000)
            .attrTween('d', tweenPie)
            .transition()
            .ease('elastic')
            .delay(function (d, i) {
                return 2000 + i * 50;
            })
            .duration(750)
            .attrTween('d', tweenDonut);

        function tweenPie(b) {
            b.innerRadius = 0;
            var i = d3.interpolate({
                startAngle: 0,
                endAngle: 0
            }, b);
            return function (t) {
                return arc(i(t));
            };
        }

        function tweenDonut(b) {
            b.innerRadius = radius * .6;
            var i = d3.interpolate({
                innerRadius: 0
            }, b);
            return function (t) {
                return arc(i(t));
            };
        }

        $('.chart-education').append(template('userEducation', data));
    }

    return {
        draw: draw
    };
});