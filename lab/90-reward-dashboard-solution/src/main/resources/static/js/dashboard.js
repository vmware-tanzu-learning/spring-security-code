$.getJSON("/rewards", function(rewards) {
        createRewardsPieChart(rewards);
    })
    .fail(function(e) {
        console.log(e);
        $("#error").text("Oh no! Status code: " + e.status);
    });


function createRewardsPieChart(rewards) {
    let restaurantsMap = new Map();

    rewards.forEach(function(reward) {
        var restaurantRewards = restaurantsMap.get(reward.restaurant.name);
        if (!restaurantRewards) {
            restaurantsMap.set(reward.restaurant.name, []);
            restaurantRewards = restaurantsMap.get(reward.restaurant.name);
        }
        restaurantRewards.push(reward);
    });

    let rewardsTotals = Array.from(restaurantsMap.values()).map(arr => arr.reduce((sum, reward) => sum += reward.amount, 0));

    var restaurantsPie = {
        labels: Array.from(restaurantsMap.keys()),
        datasets: [{
            label: "Rewards By Restaurant",
            data: rewardsTotals,
            backgroundColor: [
                "#DEB887",
                "#A9A9A9",
                "#DC143C"
            ],
            borderColor: [
                "#CDA776",
                "#989898",
                "#CB252B",
            ],
            borderWidth: [1, 1, 1]
        }]
    };

    //options
    var options = {
        responsive: true,
        title: {
            display: true,
            position: "top",
            text: "Rewards By Restaurant",
            fontSize: 18,
            fontColor: "#111"
        },
        legend: {
            display: true,
            position: "bottom",
            labels: {
                fontColor: "#333",
                fontSize: 16
            }
        }
    };
    var ctx = document.getElementById('pie-chart')

    var chart1 = new Chart(ctx, {
        type: "pie",
        data: restaurantsPie,
        options: options
    });
}