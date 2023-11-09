let number = document.getElementById("number");
let counter = 0;
const circle = document.querySelector('circle');
const totalLength = circle.getTotalLength();
function setProgress(progress) {
    circle.style.strokeDashoffset = totalLength - (progress / 14) * totalLength;

    let circleAnimation = circle.animate(
        [
            {strokeDashoffset: totalLength}, // Start with the initial value
            {strokeDashoffset: totalLength - (progress / 14) * totalLength} // End with the desired value (e.g., '0' for a full circle)
        ],
        {
            duration: 500, // Animation duration in milliseconds
            fill: 'forwards' // Animation should stay at the final state
        }
    );

    circleAnimation.play();
}

setProgress(parseInt(number.textContent));

setInterval(() => {
    if (counter === 5) {
        clearInterval();
    } else {
        counter += 1;
        number.innerHTML = counter + " / 14";
    }
}, 100)

