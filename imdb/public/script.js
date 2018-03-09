// defaults
var DEFAULT_GENERE = "Action";
var DEFAULT_ROLE = "actor";
var DEFAULT_DECADE = "60";
var myChart = null;

//global variables
var genre = DEFAULT_GENERE;
var role = DEFAULT_ROLE;
var decade = DEFAULT_DECADE;
var canvasWidth = 600;
var canvasHeight = 400;


///////////////////////

//functions
function setGenre(elem) {
    if (elem.value != "") {
        genre = elem.value;
        console.log("chose genre: " + genre);
    }
}

function setRole(elem) {
    if (elem.value != "") {
        role = elem.value;
        console.log("chose role: " + role);
    }
}

function setDecade(elem) {
    if (elem.value != "") {
        decade = elem.value;
        console.log("chose decade: " + decade);
    }
}

function execute() {

    var years = [];
    var year = 0;
    if (parseInt(decade) > 10) {
        year = 1900 + parseInt(decade);
    } else {
        year = 2000 + parseInt(decade);
    }
    for (var i = 0; i < 10; i++) {
        years.push(year + i);
    }
    $.get("http://localhost:8080/" + genre + "/" + role + "/" + decade, function (response) {
        //do something with response
        console.log(response);
        buildGraph(response, years);
    });


    $.get("http://localhost:8080/" + genre + "/" + decade, function (response) {
        //do something with response
        console.log(response);
        addMovieList(response);

    });
}
Chart.defaults.global.animation.duration = 1000; //animation duration in milliseconds
function buildGraph(arr, years) {
    if (myChart != null) {
        myChart.destroy();
    }
///////////////////////
// For drawing the lines
    var male = [];
    var yearsFinal = []
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] != -1) {
            male.push(arr[i]);
            yearsFinal.push(years[i]);
        }
    }
    var female = [];
    for (var i = 0; i < male.length; i++) {
        female.push(100 - male[i]);
    }

    var ctx = $("#myChart");
    myChart = new Chart(ctx, {

        type: 'line',
        data: {
            labels: yearsFinal,
            datasets: [
                {
                    data: male,
                    label: "Male",
                    borderColor: "#3e95cd",
                    fill: false
                },
                {
                    data: female,
                    label: "Female",
                    borderColor: "#8e5ea2",
                    fill: false
                },

            ]
        },
        options: {
            animationEasing: 'linear',
            responsive: true,
            scales: {
                yAxes : [{
                    ticks : {
                        max : 100,
                        min : 0,
                        stepSize: 5
                    }
                }]
            }
        }
    });

}

function addMovieList(movies) {
    $("#movieListContainer").empty()
    $("#movieListContainer").append(getHtml(movies));
}

function getHtml(movies) {
    var html = "<div id='movies' title='List of movies used'><p>"
    movies.forEach(
        movie => {
        html += "<a href='" + "https://www.themoviedb.org/movie/" + movie.id + "-" + movie.name.trim().replace(" ", "-") + "'>" + movie.name + " (" + movie.date + ")<br></a>";
    });
    html += "</p></div>";
    return html;
}

