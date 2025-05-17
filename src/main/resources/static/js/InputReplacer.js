class InputReplacer {
    static originalInputs = [];
    static replaceInputsWithText() {
        const container = document.getElementById("contact-fields");
        const fields = container.querySelectorAll(".contact-field");

        fields.forEach(field => {
            const input = field.querySelector("input");
            const icon = field.querySelector("i");

            if (input && icon) {
                const value = input.value.trim();
                field.innerHTML = '';

                if (value !== '') {
                    const newIcon = document.createElement("i");
                    newIcon.className = icon.className;

                    const span = document.createElement("span");
                    span.textContent = value;
                    span.style.marginRight = "40px";

                    field.appendChild(newIcon);
                    field.appendChild(span);
                } else {
                    field.remove();
                }
            }
        });

        contactFieldsAdded = 0;
    }

    static replaceTextWithInputs() {
        if (!originalInputs || originalInputs.length === 0) {
            return;
        }

        originalInputs.forEach(entry => {
            entry.parent.replaceChild(entry.input, entry.clone);
        });

        originalInputs = [];
    }

}