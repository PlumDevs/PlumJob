class ContactManager {
    static  contactFieldsAdded = 0;


    static addContactField(type) {
        if (contactFieldsAdded >= 4) {
            alert("You can only add up to 4 contact fields.");
            return;
        }
        const fields = {
            email: {
                icon: "fas fa-envelope",
                placeholder: "your.email@gmail.com",
                inputType: "email",
                id: "contact-email"
            },
            phone: {icon: "fas fa-phone", placeholder: "+1 000 000 0000", inputType: "text", id: "contact-phone"},
            address: {
                icon: "fas fa-home",
                placeholder: "Address, City, Country",
                inputType: "text",
                id: "contact-address"
            },
            linkedin: {
                icon: "fab fa-linkedin",
                placeholder: "linkedin.com/in/yourprofile",
                inputType: "text",
                id: "contact-linkedin"
            },
            github: {
                icon: "fab fa-github",
                placeholder: "github.com/yourgithub",
                inputType: "text",
                id: "contact-github"
            }
        };

        if (!fields[type]) return;

        const container = document.getElementById("contact-fields");

        if (document.getElementById(`contact-${type}`)) return;

        const wrapper = document.createElement("div");
        wrapper.className = "contact-field";
        wrapper.id = `contact-${type}`;

        wrapper.innerHTML = `
        <i class="${fields[type].icon}"></i>
        <input type="${fields[type].inputType}" placeholder="${fields[type].placeholder}" id="${fields[type].id}"
               oninput="autoGrow('${fields[type].id}')" />
    `;

        container.appendChild(wrapper);
        updateIconColor(type);
        contactFieldsAdded += 1;
    }

    static updateIconColor(type) {
        let color;

        switch (type) {
            case 'email':
                color = 'blue';
                break;
            case 'phone':
                color = 'green';
                break;
            case 'address':
                color = 'red';
                break;
            case 'linkedin':
                color = '#0077b5'; // LinkedIn blue
                break;
            case 'github':
                color = 'black';
                break;
            default:
                color = 'black';
        }

        let icon = document.querySelector(`.fa-${type}`);
        if (icon) {
            icon.style.color = color;
        }
    }



    static finalizeContactFields() {
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
                    span.style.marginLeft = "5px";

                    field.appendChild(newIcon);
                    field.appendChild(span);
                } else {
                    field.remove();
                }
            }
        });

        contactFieldsAdded = 0;

        const options = document.getElementById("contact-options");
        options.style.animation = "fadeOut 0.5s ease";
        setTimeout(() => {
            options.style.display = "none";
        }, 500);
    }




    static removeContactField(type) {
        const field = document.getElementById(`contact-${type}`);
        if (field) field.remove();
    }


    static finalizeContactFields() {
        document.getElementById("contact-options").style.display = "none";
    }

}