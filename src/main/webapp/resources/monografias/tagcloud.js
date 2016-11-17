var generateTagCloud = function(){
    $("#tags").html("Gerando tag cloud...");

    var fill = d3.scale.category20();

    function draw(words) {
        $("#tags").html("");
        d3.select("#tags").append("svg")
            .attr("width", layout.size()[0])
            .attr("height", layout.size()[1])
            .attr("xmlns","http://www.w3.org/2000/svg")
            .attr("xlink", "http://www.w3.org/1999/xlink")
            .append("g")
            .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
            .selectAll("text")
            .data(words)
            .enter().append("a")
            .attr("xlink:href", function(d){ return "?q=" + d.text })
            .style("text-decoration","none")
            .append("text")
            .style("font-size", function(d) { return d.size + "px"; })
            .style("font-family", "sans-serif")
            .style("fill", function(d, i) { return fill(i); })
            .attr("transform", function(d) {
                return "translate(" + [d.x, d.y] + ")";
            })
            .attr("text-anchor", "middle")
            .text(function(d) { return d.text; });
    }

    var sumOccurences = 0;

    $.each(tags, function(i, tag){
        sumOccurences += tag.ocorrencias;
    });

    var avgOccurences = sumOccurences / tags.length;

    var container = $("div.container").first();

    var layout = cloud()
        .size([$(container).width(), $(window).height() - 400])
        .words(tags.filter(function (x) {
            return !(/^\d+$/.test(x.tag));
        }).map(function(t) {
            return {text: t.tag, size: Math.round(20 * Math.pow(t.ocorrencias / avgOccurences, 3))};
        }))
        .padding(10)
        .rotate(0)
        .font("sans-serif")
        .fontSize(function(d) { return d.size; })
        .on("end", draw);

    layout.start();
};

$(generateTagCloud);

var resizeTimeout;

$(window).resize(function(){
    clearTimeout(resizeTimeout);
    resizeTimeout = setTimeout(generateTagCloud, 100);
});