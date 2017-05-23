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
        data = JSON.parse(data);

        var dataset = data.groups;
        var width = 330;
        var height = 330;

        var svg = d3.select('.chart-gender .svg-container')
            .append('svg')
            .attr('width', width)
            .attr('height', height);

        var padding = {
            left: 30,
            right: 30,
            top: 20,
            bottom: 20
        };
        var colors = d3.scale.category10();
        data.groups.forEach(function (item, idx) {
           item.color = colors(idx);
        });

        var pie = d3.layout.pie()
            .sort(null)
            .value(function (d) {
                return d.count;
            });
        var pieData = pie(dataset);
        var radius = 155;
        var arc = d3.svg.arc()
            .innerRadius(0)
            .outerRadius(radius);

        var paths = svg.append('g').attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ' )');
        paths.selectAll('path')
            .data(pieData)
            .enter()
            .append('path')
            .attr('fill', function (d, i) {
                return colors(i);
            })
            .attr('d', function (d) {
                return arc(d);
            });

        var textParent = svg.append('g').attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ')');
        var texts = textParent.selectAll('text')
            .data(pieData)
            .enter()
            .append('text')
            .attr({
                transform: function(d) {
                    return 'translate(' + arc.centroid(d) + ')';
                },
                'text-anchor': 'middle',
                'font-size': '10px',
            })
            .text(function(d) {
                return (d.data.rate * 100).toFixed(3) + '%';
            });

        $('.chart-gender').append(template('userGender', data));
    }

    return {
        draw: draw
    };
});