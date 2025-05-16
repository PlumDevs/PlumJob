class PDFGenerator {

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

    static restoreInputs() {
        PDFGenerator.originalInputs.forEach(item => {
            item.parent.replaceChild(item.input, item.clone);
        });
    }

    static generatePDF(event) {
        event.preventDefault();

        const buttons = document.querySelectorAll('button');
        buttons.forEach(btn => btn.style.display = 'none');

        // Wywołanie replaceInputsWithText z klasy PDFGenerator
        PDFGenerator.replaceInputsWithText();

        const cvContainer = document.querySelector('.cv-container');

        const opt = {
            margin: [0, 0, 0, 0],
            filename: 'CV.pdf',
            image: { type: 'jpeg', quality: 1 },
            html2canvas: { scale: 4, useCORS: true, letterRendering: true },
            jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' },
            enableLinks: true,
            pagebreak: { mode: ['css', 'legacy'] }
        };

        html2pdf().from(cvContainer).set(opt).save()
            .then(() => {
                // Wywołanie restoreInputs z klasy PDFGenerator
                PDFGenerator.restoreInputs();
                buttons.forEach(btn => btn.style.display = 'inline-block');
            })
            .catch((error) => {
                console.error('Błąd podczas generowania PDF:', error);
                PDFGenerator.restoreInputs();
                buttons.forEach(btn => btn.style.display = 'inline-block');
            });
    }
}
