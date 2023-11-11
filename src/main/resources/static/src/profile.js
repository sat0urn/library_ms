let number = document.getElementById("number");
let counter = 0;
const circle = document.querySelector('circle');
const totalLength = circle.getTotalLength();
function setProgress(progress) {
    circle.style.strokeDashoffset = (totalLength - (progress / 14) * totalLength).toString();

    let circleAnimation = circle.animate(
        [
            {strokeDashoffset: totalLength}, // Start with the initial value
            {strokeDashoffset: totalLength - (progress / 14) * totalLength} // End with the desired value (e.g., '0' for a full circle)
        ],
        {
            duration: 850, // Animation duration in milliseconds
            fill: 'forwards' // Animation should stay at the final state
        }
    );

    setInterval(() => {
        if (counter === progress) {
            clearInterval();
        } else {
            counter += 1;
            number.innerHTML = counter + " / 14";
        }
    }, 60)
    circleAnimation.play();
}

setProgress(parseInt(number.textContent));

