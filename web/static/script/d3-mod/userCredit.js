define(function () {
    function draw() {
        $.ajax({
            url: 'api/getUserEducation.json',
            success: render
        });
    }

    function render(data) {
        data = JSON.parse(data);
        var width = 500;
        var height = 500;
        var svg = d3.select('.chart-credit')
            .append('svg')
            .attr('width', width)
            .attr('height', height);
        var padding = {
            left: 30,
            right: 30,
            top: 20,
            bottom: 20
        };
        var barHeight = 20;
        var dataset = [];
        data.forEach(function(item) {
            dataset.push(item.count);
        });

        var scale = d3.scale.linear().domain([0, d3.max(dataset)]).range([0, width - padding.left - padding.right]);

        svg.selectAll('.rect')
            .data(data)
            .enter()
            .append('rect')
            .attr({
                class: 'rect',
                x: padding.left,
                y: function(d, i) {
                    return i*barHeight;
                },
                width: function(d) {
                    return scale(d.count);
                },
                height: barHeight
            });
    }

    return {
        draw: draw
    };
});