let image_input = document.getElementById("image_input");
let image_submit_btn = document.getElementById("image_submit_btn");

image_input.addEventListener('change', function(e) {
    if (e.target.files[0]) {
        image_submit_btn.classList.remove("d-none");
    }
});