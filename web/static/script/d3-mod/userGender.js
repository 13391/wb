/**
 * 用户性别分析模块
 */

define(function () {

    function draw() {
        $.ajax({
            url: 'api/getUserGender.json',
            success: render
        });
    }

    function render(data) {

        var sum = 0;
        data = JSON.parse(data);
        data.forEach(function (i) {
            sum += i.count;
        });
        data.forEach(function (item) {
            item.rate = (item.count / sum).toFixed(3);
        });

        var chartData = [];
        data.forEach(function (i) {
            chartData.push(i.count);
        });

        var width = 350,
            height = 350,
            radius = Math.min(width, height) / 2 - 10;

        var color = d3.scale.category20();

        var arc = d3.svg.arc()
            .outerRadius(radius);

        var pie = d3.layout.pie();

        var svg = d3.select('.chart-gender .svg-container').append('svg')
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

        $('.chart-gender').append(template('userGender', {
            total: sum,
            groups: data
        }));
    }

    return {
        draw: draw
    };
});