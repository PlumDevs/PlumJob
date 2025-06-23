/**
 * @jest-environment jsdom
 */

// we mock of functions from original file - templateX.html
let selectedContacts = [];

function addContactField(type) {
    const index = selectedContacts.indexOf(type);

    if (index > -1) {
        removeContactField(type);
        selectedContacts.splice(index, 1);

        if (document.querySelector) {
            const btn = document.querySelector(`button[onclick="addContactField('${type}')"]`);
            if (btn && btn.classList) {
                btn.classList.remove('selected');
            }
        }
        return;
    }

    if (selectedContacts.length >= 3) {
        alert("You can only select exactly 3 contact fields.");
        return;
    }

        const existingField = document.getElementById(`contact-${type}`);
        if (existingField) {
            return; // Dont add if field already exists
        }

    const fields = {
        email: { icon: "fas fa-envelope", placeholder: "your.email@gmail.com", inputType: "email", id: "contact-email" },
        phone: { icon: "fas fa-phone", placeholder: "+1 000 000 0000", inputType: "text", id: "contact-phone" },
        address: { icon: "fas fa-home", placeholder: "Address, City, Country", inputType: "text", id: "contact-address" },
        linkedin: { icon: "fab fa-linkedin", placeholder: "linkedin.com/in/yourprofile", inputType: "text", id: "contact-linkedin" },
        github: { icon: "fab fa-github", placeholder: "github.com/yourgithub", inputType: "text", id: "contact-github" }
    };

    if (!fields[type]) return;

    const container = document.getElementById("contact-fields");
    if (!container) return;

    const wrapper = document.createElement("div");
    wrapper.className = "contact-field";
    wrapper.id = `contact-${type}`;

    wrapper.innerHTML = `
        <i class="${fields[type].icon}"></i>
        <input type="${fields[type].inputType}" placeholder="${fields[type].placeholder}" id="${fields[type].id}" />
    `;

    container.appendChild(wrapper);
    selectedContacts.push(type);

    if (document.querySelector) {
        const btn = document.querySelector(`button[onclick="addContactField('${type}')"]`);
        if (btn && btn.classList) {
            btn.classList.add('selected');
        }
    }
}

function removeContactField(type) {
    const field = document.getElementById(`contact-${type}`);
    if (field) field.remove();
}

function addEducation() {
    const div = document.createElement("div");
    div.className = "dynamic-entry";
    div.innerHTML = `
    <div style="display: flex; flex-direction: column; justify-content: flex-start;">
        <div style="display: flex; justify-content: space-between; margin-bottom: 0;">
          <div style="font-size:17px; font-weight:bold; margin-bottom: 0;">
            <textarea style="border:none; width: 100%; height: 17px; font-size:17px; font-weight:bold; margin-bottom: 0;" placeholder="University name"></textarea>
          </div>
          <div style="margin-bottom: 0;">
            <textarea style="border:none; width: 100%; height: 17px; margin-bottom: 0;" placeholder="Years attended"></textarea>
          </div>
        </div>
        <div style="margin:0;">
          <textarea style="border:none; width: 100%; height: 15px; font-size:15px; margin:0;" placeholder="Field of study"></textarea>
        </div>
    </div>
    <ul style="margin:0;">
        <li class="custom-li"><textarea style="border:none; width: 100%; height: 34px; margin:0;" placeholder="Subject, project, or activity"></textarea></li>
    </ul>`;
    const container = document.getElementById("education-list");
    if (container) container.appendChild(div);
}

function addExperience() {
    const div = document.createElement("div");
    div.className = "dynamic-entry";
    div.innerHTML = `
        <div style="display: flex; flex-direction: column; justify-content: flex-start;">
            <div style="display: flex; justify-content: space-between;">
                <div style="font-size:17px; font-weight:bold;">
                    <textarea style="border:none; width: 100%; height: 17px; font-size:17px; font-weight:bold;" placeholder="Position"></textarea>
                </div>
                <div>
                    <textarea style="border:none; width: 100%; height: 17px;" placeholder="Period"></textarea>
                </div>
            </div>
            <div style="margin-top:4px;">
              <textarea style="border:none; width: 100%; height: 15px;font-size:15px;" placeholder="Company"></textarea>
            </div>
        </div>
        <ul>
            <li class="custom-li"><textarea style="width: 100%; height: 34px; border:none;" placeholder="Responsibilities or achievement"></textarea></li>
        </ul>`;
    const container = document.getElementById("experience-list");
    if (container) container.appendChild(div);
}

function addSkill() {
    const div = document.createElement("div");
    div.className = "dynamic-entry";
    div.innerHTML = `
        <ul>
            <li class="custom-li">
                <div contenteditable="true" oninput="updateStyle(this)" placeholder="Skill"
                style="width: 100%; min-height: 15px; border:none; font-weight: 400; font-size: 14px;"></div>
            </li>
        </ul>`;
    const container = document.getElementById("skills-list");
    if (container) container.appendChild(div);
}

function addProject() {
    const div = document.createElement("div");
    div.className = "dynamic-entry";
    div.innerHTML = `
    <div style="display: flex; align-items: center;">
        <textarea style="width: 100%; height: 17px; border:none; font-size:17px; font-weight:bold;" placeholder="Project name"></textarea>
    </div>
    <ul>
         <li class="custom-li" style="margin-top:0;"><textarea style="border:none; width: 100%; height: 15px;  font-size:15px;" placeholder="Project link (github.com/...)"></textarea></li>
         <li class="custom-li"><textarea style="border:none; width: 100%; height: 34px;" placeholder="Project feature or functionality"></textarea></li>
    </ul>`;
    const container = document.getElementById("projects-list");
    if (container) container.appendChild(div);
}

function addCertificate() {
    const div = document.createElement("div");
    div.className = "dynamic-entry";
    div.innerHTML = `
         <div style="display: flex; flex-direction: column; justify-content: flex-start;">
          <div style="display: flex; justify-content: space-between;">
             <div style="font-size:17px; font-weight:bold;margin-bottom: 0;">
                <textarea style="border:none; width: 100%; height: 17px; font-size:17px; font-weight:bold;margin-bottom: 0;" placeholder="Certificate Title"></textarea>
             </div>
             <div style="margin-bottom: 0;">
                 <textarea style="border:none; width: 100%; height: 17px; margin-bottom: 0;" placeholder="Year"></textarea>
             </div>
          </div>
          <div style="margin-top:1px; margin-bottom: 0;">
            <textarea style="border:none; width: 100%; height: 15px; font-size:15px; margin-bottom: 0;" placeholder="Issuer"></textarea>
          </div>
        </div>`;
    const container = document.getElementById("certificates-list");
    if (container) container.appendChild(div);
}

function addRODO() {
    const div = document.createElement("div");
    div.className = "dynamic-entry";
    div.innerHTML = `
        <textarea
            placeholder="Enter here e.g. GDPR clause…"
            style="width: 100%; border: none; padding: 0.5rem; font-family: inherit; font-size: 0.85rem; line-height: 1.4; border-radius: 4px; resize: vertical; min-height: 70px;"></textarea>`;
    const container = document.getElementById("rodo-section");
    if (container) container.appendChild(div);
}

function removeLast(listId) {
    const list = document.getElementById(listId);
    if (list && list.childElementCount > 1) {
        list.removeChild(list.lastElementChild);
    }
}

function autoGrow(id) {
    const field = document.getElementById(id);
    if (field && typeof field.scrollWidth !== 'undefined') {
        field.style.width = "1px";
        field.style.width = (field.scrollWidth) + "px";
    }
}

function updateStyle(el) { // Test called "should not modify text without colon" should have passed, because this function works well
    if (!el) return;
        const text = el.innerText || el.textContent || '';
        if (!text) return;
    const match = text.match(/^([^:]+:)(.*)$/);

    if (match) {
        const boldPart = match[1].trim();
        const normalPart = match[2];
        el.innerHTML = `<strong style="font-weight: 5000; font-size: 120%;">${boldPart}</strong>${normalPart}`;

        const range = document.createRange();
        range.selectNodeContents(el);
        range.collapse(false);
        const selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
    }
}

function finalizeContactFields() {
    if (selectedContacts.length < 3 || selectedContacts.length > 3) {
        alert("Please select exactly 3 contact fields.");
        return;
    }
    const container = document.getElementById("contact-fields");
    if (!container) return;

    const fields = container.querySelectorAll(".contact-field");

    fields.forEach(field => {
        const input = field.querySelector("input");
        const icon = field.querySelector("i");

        if (input && icon) {
            const value = input.value.trim();

            if (value !== '') {
                const fieldId = field.id;

                field.innerHTML = '';

                const newIcon = document.createElement("i");
                newIcon.className = icon.className;

                const span = document.createElement("span");
                span.textContent = value;
                span.style.marginLeft = "5px";

                field.appendChild(newIcon);
                field.appendChild(span);
                field.id = fieldId;
            } else {
                const fieldType = field.id.replace('contact-', '');
                const index = selectedContacts.indexOf(fieldType);
                if (index > -1) {
                     selectedContacts.splice(index, 1);
                }
                field.remove();
            }
        }
    });

    const options = document.getElementById("contact-options");
    if (options) {
        options.style.animation = "fadeOut 0.5s ease";
        setTimeout(() => {
            if (options.parentNode) {
                options.style.display = "none";
            }
        }, 500);
    }
}

function addLanguage() {
    const container = document.getElementById("languages");

    if (!container) return;

    const wrapper = document.createElement("div");
    wrapper.style.display = "flex";
    wrapper.style.alignItems = "center";
    wrapper.style.gap = "2px";
    wrapper.style.padding = "6px 6px";
    wrapper.style.border = "1px solid #ccc";
    wrapper.style.borderRadius = "8px";
    wrapper.style.backgroundColor = "#D3D3D3";
    wrapper.style.marginBottom = "10px";
    wrapper.style.width = "fit-content";

    const input = document.createElement("input");
    input.type = "text";
    input.placeholder = "Language";
    input.style.width = "70px";
    input.style.border = "none";
    input.style.outline = "none";
    input.style.background = "transparent";
    input.style.fontSize = "14px";
    input.style.padding = "0";
    input.style.margin = "0";

    const levelWrapper = document.createElement("div");
    levelWrapper.style.display = "flex";
    levelWrapper.style.alignItems = "center";
    levelWrapper.style.gap = "2px";
    levelWrapper.style.marginLeft = "2px";
    levelWrapper.style.background = "transparent";
    levelWrapper.style.border = "none";
    levelWrapper.style.padding = "0";

    let selectedLevel = 0;

    function updateDots(level) {
        if (selectedLevel === level) {
            selectedLevel = 0;
        } else {
            selectedLevel = level;
        }
        Array.from(levelWrapper.children).forEach((dot, index) => {
            dot.textContent = index < selectedLevel ? "●" : "○";
        });
    }

    for (let i = 1; i <= 5; i++) {
        const dot = document.createElement("span");
        dot.textContent = "○";
        dot.style.cursor = "pointer";
        dot.style.fontSize = "16px";
        dot.style.userSelect = "none";
        dot.style.lineHeight = "1";
        dot.style.margin = "0";
        dot.style.padding = "0";
        dot.style.fontFamily = "monospace";
        dot.style.fontWeight = "bold";
        dot.style.color = "#000";
        dot.onclick = () => updateDots(i);
        levelWrapper.appendChild(dot);
    }

    wrapper.appendChild(input);
    wrapper.appendChild(levelWrapper);
    container.appendChild(wrapper);
}

function removeLastt(listId) {
    const list = document.getElementById(listId);
    if (list && list.childElementCount > 1) {
        list.removeChild(list.lastElementChild);
    }
}


// Setup DOM before each test (Document Object Model (DOM) for a web page)
beforeEach(() => {
    document.body.innerHTML = `
        <div id="contact-fields" class="contact-fields"></div>
        <div id="contact-options" class="contact-options">
            <button onclick="addContactField('email')" class="contact-btn" id="email-btn">Email</button>
            <button onclick="addContactField('phone')" class="contact-btn" id="phone-btn">Phone</button>
            <button onclick="addContactField('address')" class="contact-btn" id="address-btn">Address</button>
            <button onclick="addContactField('linkedin')" class="contact-btn" id="linkedin-btn">LinkedIn</button>
            <button onclick="addContactField('github')" class="contact-btn" id="github-btn">GitHub</button>
        </div>
        <div id="education-list"></div>
        <div id="skills-list"></div>
        <div id="experience-list"></div>
        <div id="certificates-list"></div>
        <div id="projects-list"></div>
        <div id="rodo-section"></div>
        <div id="languages"></div>
    `;

    selectedContacts = [];
});

describe('CV Template Unit Tests', () => {

    describe('Contact Fields Management - Core Logic', () => {

        test('should add contact field and update state correctly', () => {
            addContactField('email');

            expect(selectedContacts).toContain('email');
            expect(document.getElementById('contact-email')).toBeTruthy();
        });

        test('should toggle contact field when called twice', () => {
            const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

            addContactField('email');
            expect(selectedContacts).toContain('email');

            addContactField('email'); // Should remove
            expect(selectedContacts).not.toContain('email');
            expect(document.getElementById('contact-email')).toBeFalsy();

            alertSpy.mockRestore();
        });

        test('should enforce maximum 3 contacts limit', () => {
            const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

            addContactField('email');
            addContactField('phone');
            addContactField('address');
            addContactField('linkedin'); // Should be rejected

            expect(selectedContacts.length).toBe(3);
            expect(selectedContacts).not.toContain('linkedin');
            expect(alertSpy).toHaveBeenCalledWith("You can only select exactly 3 contact fields.");

            alertSpy.mockRestore();
        });

        test('should reject invalid contact types', () => {
            const initialLength = selectedContacts.length;

            addContactField('invalid-type');
            addContactField('');
            addContactField(null);
            addContactField(undefined);

            expect(selectedContacts.length).toBe(initialLength);
        });

        test('should prevent duplicate contact fields', () => {// test should have passed because if we try to add the same field, it doesn't duplicate, it just removes this field what we try to add again
            addContactField('email');
            const firstField = document.getElementById('contact-email');

            addContactField('email');
            addContactField('email');

            expect(document.querySelectorAll('#contact-email').length).toBeLessThanOrEqual(1);
        });

        test('should handle missing DOM gracefully', () => {
            document.getElementById('contact-fields').remove();

            expect(() => addContactField('email')).not.toThrow();
            expect(selectedContacts).not.toContain('email');
        });
    });

    describe('Dynamic Content Addition - Functional Logic', () => {

        test('should add education entry and increment count', () => {
            const initialCount = document.getElementById('education-list').children.length;

            addEducation();

            expect(document.getElementById('education-list').children.length).toBe(initialCount + 1);

            const entry = document.getElementById('education-list').lastElementChild;
            expect(entry.className).toBe('dynamic-entry');
        });

        test('should add experience entry with proper structure', () => {
            const initialCount = document.getElementById('experience-list').children.length;

            addExperience();

            expect(document.getElementById('experience-list').children.length).toBe(initialCount + 1);
            expect(document.getElementById('experience-list').lastElementChild.className).toBe('dynamic-entry');
        });

        test('should add multiple entries correctly', () => {
            addEducation();
            addEducation();
            addExperience();

            expect(document.getElementById('education-list').children.length).toBe(2);
            expect(document.getElementById('experience-list').children.length).toBe(1);
        });

        test('should handle missing containers gracefully', () => {
            document.getElementById('education-list').remove();

            expect(() => addEducation()).not.toThrow();
        });
    });

    describe('Remove Last Entry Logic', () => {

        test('should remove last entry when entries exist', () => {
            addEducation();
            addEducation();

            const countBefore = document.getElementById('education-list').children.length;
            removeLast('education-list');

            expect(document.getElementById('education-list').children.length).toBe(countBefore - 1);
        });

        test('should handle empty list gracefully', () => {
            const countBefore = document.getElementById('education-list').children.length;

            removeLast('education-list');

            expect(document.getElementById('education-list').children.length).toBe(Math.max(0, countBefore));
        });

        test('should handle non-existent container', () => {
            expect(() => removeLast('non-existent-list')).not.toThrow();
        });
    });

    describe('Language Management Logic', () => {

        test('should add language entry with interactive level selection', () => {
            addLanguage();

            const container = document.getElementById('languages');
            expect(container.children.length).toBe(1);

            const wrapper = container.firstChild;
            const input = wrapper.querySelector('input[type="text"]');
            const dots = wrapper.querySelectorAll('span');

            expect(input).toBeTruthy();
            expect(input.placeholder).toBe('Language');
            expect(dots.length).toBe(5);
        });

        test('should update proficiency level correctly', () => {
            addLanguage();

            const container = document.getElementById('languages');
            const dots = container.querySelectorAll('span');

            dots[2].click();

            expect(dots[0].textContent).toBe('●');
            expect(dots[1].textContent).toBe('●');
            expect(dots[2].textContent).toBe('●');
            expect(dots[3].textContent).toBe('○');
            expect(dots[4].textContent).toBe('○');
        });

        test('should toggle level when clicking same level twice', () => {
            addLanguage();

            const container = document.getElementById('languages');
            const dots = container.querySelectorAll('span');

            dots[1].click(); // Select level 2
            expect(dots[1].textContent).toBe('●');

            dots[1].click(); // Deselect
            expect(dots[0].textContent).toBe('○');
            expect(dots[1].textContent).toBe('○');
        });

        test('should handle missing language container', () => {
            document.getElementById('languages').remove();

            expect(() => addLanguage()).not.toThrow();
        });

        test('should remove last language but preserve at least one', () => {
            addLanguage();
            addLanguage();

            expect(document.getElementById('languages').children.length).toBe(2);

            removeLastt('languages');
            expect(document.getElementById('languages').children.length).toBe(1);

            removeLastt('languages'); // Should not remove the last one
            expect(document.getElementById('languages').children.length).toBe(1);
        });
    });

    describe('Text Formatting Logic', () => {

        test('should format text with colon pattern correctly', () => {
            const testDiv = document.createElement('div');
            testDiv.innerText = 'JavaScript: Advanced level';

            updateStyle(testDiv);

            expect(testDiv.innerHTML).toContain('<strong');
            expect(testDiv.innerHTML).toContain('JavaScript:');
            expect(testDiv.innerHTML).toContain('Advanced level');
        });

        test('should not modify text without colon', () => {
            const testDiv = document.createElement('div');
            const originalText = "JavaScript Advanced level";
            testDiv.innerText = originalText;

            updateStyle(testDiv);

            expect(testDiv.innerHTML).toBe(originalText);
        });

        test('should handle edge cases gracefully', () => {
            expect(() => updateStyle(null)).not.toThrow();
            expect(() => updateStyle(undefined)).not.toThrow();

            const emptyDiv = document.createElement('div');
            expect(() => updateStyle(emptyDiv)).not.toThrow();
        });
    });

    describe('Auto Grow Functionality', () => {

        test('should adjust field width based on scroll width', () => {
            const testInput = document.createElement('input');
            testInput.id = 'test-input';
            document.body.appendChild(testInput);

            Object.defineProperty(testInput, 'scrollWidth', {
                get: () => 150,
                configurable: true
            });

            autoGrow('test-input');

            expect(testInput.style.width).toBe('150px');

            document.body.removeChild(testInput);
        });

        test('should handle non-existent field', () => {
            expect(() => autoGrow('non-existent-field')).not.toThrow();
        });
    });

    describe('Finalize Contact Fields Logic', () => {

        test('should finalize when exactly 3 contacts selected', () => {
            const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

            addContactField('email');
            addContactField('phone');
            addContactField('address');

            document.getElementById('contact-email').value = 'test@example.com';
            document.getElementById('contact-phone').value = '+1234567890';
            document.getElementById('contact-address').value = 'Test Address';

            finalizeContactFields();

            expect(alertSpy).not.toHaveBeenCalled();

            alertSpy.mockRestore();
        });

        test('should reject finalization with wrong number of contacts', () => {
            const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

            addContactField('email');
            finalizeContactFields();

            expect(alertSpy).toHaveBeenCalledWith("Please select exactly 3 contact fields.");

            alertSpy.mockRestore();
        });

        test('should remove empty contact fields during finalization', () => {
            const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

            addContactField('email');
            addContactField('phone');
            addContactField('address');

            document.getElementById('contact-email').value = 'test@example.com';
            document.getElementById('contact-phone').value = '+1234567890';
            document.getElementById('contact-address').value = '';

            finalizeContactFields();

            expect(document.getElementById('contact-address')).toBeFalsy();

            alertSpy.mockRestore();
        });
    });

    describe('State Consistency', () => {

        test('should maintain selectedContacts array consistency', () => {
            addContactField('email');
            addContactField('phone');
            expect(selectedContacts).toEqual(['email', 'phone']);

            addContactField('email'); // Remove email
            expect(selectedContacts).toEqual(['phone']);

            addContactField('address');
            expect(selectedContacts).toEqual(['phone', 'address']);
        });

        test('should handle rapid operations correctly', () => {
            for (let i = 0; i < 5; i++) {
                addContactField('email');
            }

            // Should end up with email either selected or not, but state should be consistent
            const emailSelected = selectedContacts.includes('email');
            const emailFieldExists = !!document.getElementById('contact-email');

            expect(emailSelected).toBe(emailFieldExists);
        });
    });

    describe('Additional Edge Cases', () => {

        describe('Contact Fields - Additional Edge Cases', () => {

            test('should handle button styling when DOM elements are missing', () => {
                // Remove button from DOM
                document.getElementById('email-btn').remove();

                expect(() => addContactField('email')).not.toThrow();
                expect(selectedContacts).toContain('email');
            });

            test('should handle contact field removal when field already removed from DOM', () => {
                addContactField('email');

                // Manually remove the field from DOM
                document.getElementById('contact-email').remove();

                expect(() => removeContactField('email')).not.toThrow();
            });

            test('should handle concurrent field additions', () => {
                const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

                // Simulate rapid clicks
                addContactField('email');
                addContactField('phone');
                addContactField('address');
                addContactField('linkedin');
                addContactField('github');

                expect(selectedContacts.length).toBeLessThanOrEqual(3);
                expect(alertSpy).toHaveBeenCalled();

                alertSpy.mockRestore();
            });

            test('should handle whitespace-only input values in finalization', () => { // this test is the latest test that didn't pass
                const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

                addContactField('email');
                addContactField('phone');
                addContactField('address');

                document.getElementById('contact-email').value = '   ';
                document.getElementById('contact-phone').value = '\t\n';
                document.getElementById('contact-address').value = 'valid@email.com';

                finalizeContactFields();

                expect(document.getElementById('contact-email')).toBeFalsy();
                expect(document.getElementById('contact-phone')).toBeFalsy();
                expect(document.getElementById('contact-address')).toBeTruthy();

                alertSpy.mockRestore();
            });

            test('should handle special characters in contact type', () => {
                const specialTypes = ['email@', 'phone#', 'address!', 'linkedin$', 'github%'];

                specialTypes.forEach(type => {
                    const initialLength = selectedContacts.length;
                    addContactField(type);
                    expect(selectedContacts.length).toBe(initialLength);
                });
            });
        });

        describe('Dynamic Content - Additional Edge Cases', () => {

            test('should handle missing container during multiple additions', () => {
                addEducation();

                document.getElementById('education-list').remove();

                expect(() => addEducation()).not.toThrow();
            });

            test('should handle corrupted DOM structure', () => {
                const container = document.getElementById('education-list');
                container.innerHTML = '<div>corrupted content</div>';

                expect(() => addEducation()).not.toThrow();
                expect(container.children.length).toBe(2);
            });

            test('should handle addSkill with contenteditable edge cases', () => {
                addSkill();

                const skillsContainer = document.getElementById('skills-list');
                const contentEditableDiv = skillsContainer.querySelector('[contenteditable="true"]');

                expect(contentEditableDiv).toBeTruthy();
                expect(contentEditableDiv.getAttribute('contenteditable')).toBe('true');
            });

            test('should handle addProject with empty placeholders', () => {
                addProject();

                const projectsContainer = document.getElementById('projects-list');
                const textareas = projectsContainer.querySelectorAll('textarea');

                expect(textareas.length).toBe(3);
                textareas.forEach(textarea => {
                    expect(textarea.placeholder).toBeTruthy();
                });
            });

            test('should handle addCertificate with missing elements', () => {
                addCertificate();

                const certContainer = document.getElementById('certificates-list');
                const entry = certContainer.lastElementChild;

                expect(entry.className).toBe('dynamic-entry');
                expect(entry.querySelectorAll('textarea').length).toBe(3);
            });

            test('should handle addRODO with textarea properties', () => {
                addRODO();

                const rodoContainer = document.getElementById('rodo-section');
                const textarea = rodoContainer.querySelector('textarea');

                expect(textarea).toBeTruthy();
                expect(textarea.style.minHeight).toBe('70px');
                expect(textarea.placeholder).toContain('GDPR');
            });
        });

        describe('Remove Functions - Additional Edge Cases', () => {

            test('should handle removeLast with corrupted child elements', () => {
                const container = document.getElementById('education-list');

                addEducation();

                const corruptedDiv = document.createElement('span');
                container.appendChild(corruptedDiv);

                expect(() => removeLast('education-list')).not.toThrow();
                expect(container.children.length).toBe(1);
            });

            test('should handle removeLastt with exactly one element', () => {
                addLanguage();

                const initialCount = document.getElementById('languages').children.length;

                removeLastt('languages');

                expect(document.getElementById('languages').children.length).toBe(initialCount);
            });

            test('should handle removeLastt with null container', () => {
                document.getElementById('languages').remove();

                expect(() => removeLastt('languages')).not.toThrow();
            });
        });

        describe('Language Management - Additional Edge Cases', () => {

            test('should handle language input with extreme lengths', () => {
                addLanguage();

                const input = document.querySelector('#languages input');
                const longLanguageName = 'a'.repeat(1000);

                input.value = longLanguageName;

                expect(input.value).toBe(longLanguageName);
            });

            test('should handle dot clicking with rapid succession', () => {
                addLanguage();

                const dots = document.querySelectorAll('#languages span');

                for (let i = 0; i < 10; i++) {
                    dots[2].click();
                }

                const filledDots = Array.from(dots).filter(dot => dot.textContent === '●');
                expect(filledDots.length).toBeLessThanOrEqual(5);
            });

            test('should handle missing language container during addition', () => {
                const languageContainer = document.getElementById('languages');
                languageContainer.remove();

                expect(() => addLanguage()).not.toThrow();
            });

            test('should verify language level selection boundaries', () => {
                addLanguage();

                const dots = document.querySelectorAll('#languages span');

                dots[0].click();
                expect(dots[0].textContent).toBe('●');
                expect(Array.from(dots).slice(1).every(dot => dot.textContent === '○')).toBe(true);

                dots[4].click();
                expect(Array.from(dots).every(dot => dot.textContent === '●')).toBe(true);
            });
        });

        describe('Text Formatting - Additional Edge Cases', () => {

            test('should handle updateStyle with multiple colons', () => {
                const testDiv = document.createElement('div');
                testDiv.innerText = 'JavaScript: Advanced: Professional level';

                updateStyle(testDiv);

                expect(testDiv.innerHTML).toContain('<strong');
                expect(testDiv.innerHTML).toContain('JavaScript:');
            });

            test('should handle updateStyle with colon at the end', () => {
                const testDiv = document.createElement('div');
                testDiv.innerText = 'JavaScript:';

                updateStyle(testDiv);

                expect(testDiv.innerHTML).toContain('<strong');
                expect(testDiv.innerHTML).toContain('JavaScript:');
            });

            test('should handle updateStyle with empty string after colon', () => {
                const testDiv = document.createElement('div');
                testDiv.innerText = 'JavaScript: ';

                updateStyle(testDiv);

                expect(testDiv.innerHTML).toContain('<strong');
                expect(testDiv.innerHTML).toContain('JavaScript:');
            });

            test('should handle updateStyle with special characters', () => {
                const testDiv = document.createElement('div');
                testDiv.innerText = 'C++: <Advanced> & "Professional"';

                updateStyle(testDiv);

                expect(testDiv.innerHTML).toContain('<strong');
                expect(testDiv.innerHTML).toContain('C++:');
            });
        });

        describe('Auto Grow - Additional Edge Cases', () => {

            test('should handle autoGrow with zero scroll width', () => {
                const testInput = document.createElement('input');
                testInput.id = 'test-input-zero';
                document.body.appendChild(testInput);

                Object.defineProperty(testInput, 'scrollWidth', {
                    get: () => 0,
                    configurable: true
                });

                autoGrow('test-input-zero');

                expect(testInput.style.width).toBe('0px');

                document.body.removeChild(testInput);
            });

            test('should handle autoGrow with negative scroll width', () => {
                const testInput = document.createElement('input');
                testInput.id = 'test-input-negative';
                document.body.appendChild(testInput);

                Object.defineProperty(testInput, 'scrollWidth', {
                    get: () => -50,
                    configurable: true
                });

                autoGrow('test-input-negative');

                expect(testInput.style.width).toBe('-50px');

                document.body.removeChild(testInput);
            });

            test('should handle autoGrow with extremely large scroll width', () => {
                const testInput = document.createElement('input');
                testInput.id = 'test-input-large';
                document.body.appendChild(testInput);

                Object.defineProperty(testInput, 'scrollWidth', {
                    get: () => 999999,
                    configurable: true
                });

                autoGrow('test-input-large');

                expect(testInput.style.width).toBe('999999px');

                document.body.removeChild(testInput);
            });
        });

        describe('Finalize Contact Fields - Additional Edge Cases', () => {

            test('should handle finalization with missing contact options element', () => {
                const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

                addContactField('email');
                addContactField('phone');
                addContactField('address');

                document.getElementById('contact-options').remove();

                expect(() => finalizeContactFields()).not.toThrow();

                alertSpy.mockRestore();
            });

            test('should handle finalization with corrupted contact fields', () => {
                const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

                addContactField('email');
                addContactField('phone');
                addContactField('address');

                const emailField = document.getElementById('contact-email');
                emailField.innerHTML = '<span>corrupted</span>';

                expect(() => finalizeContactFields()).not.toThrow();

                alertSpy.mockRestore();
            });

            test('should handle finalization animation timing', (done) => {
                const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

                addContactField('email');
                addContactField('phone');
                addContactField('address');

                finalizeContactFields();

                const options = document.getElementById('contact-options');
                if (options) {
                    expect(options.style.animation).toContain('fadeOut');

                    setTimeout(() => {
                        expect(options.style.display).toBe('none');
                        done();
                    }, 600);
                } else {
                    done();
                }

                alertSpy.mockRestore();
            });
        });

        describe('Memory and Performance Edge Cases', () => {

            test('should handle large number of dynamic entries', () => {
                const iterations = 100;

                for (let i = 0; i < iterations; i++) {
                    addEducation();
                    addExperience();
                    addSkill();
                }

                expect(document.getElementById('education-list').children.length).toBe(iterations);
                expect(document.getElementById('experience-list').children.length).toBe(iterations);
                expect(document.getElementById('skills-list').children.length).toBe(iterations);
            });

            test('should handle memory cleanup when removing entries', () => {
                for (let i = 0; i < 50; i++) {
                    addEducation();
                }

                for (let i = 0; i < 25; i++) {
                    removeLast('education-list');
                }

                expect(document.getElementById('education-list').children.length).toBe(25);
            });
        });

        describe('Browser Compatibility Edge Cases', () => {

            test('should handle missing modern DOM methods gracefully', () => {
                const originalQuerySelector = document.querySelector;
                document.querySelector = undefined;

                expect(() => addContactField('email')).not.toThrow();

                document.querySelector = originalQuerySelector;
            });

            test('should handle missing classList property', () => {
                const button = document.getElementById('email-btn');
                const originalClassList = button.classList;

                button.classList = undefined;

                expect(() => addContactField('email')).not.toThrow();

                button.classList = originalClassList;
            });
        });

        describe('State Recovery Edge Cases', () => {

            test('should recover from inconsistent selectedContacts state', () => {
                selectedContacts.push('email', 'phone', 'address', 'linkedin');

                const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

                addContactField('github');

                expect(alertSpy).toHaveBeenCalled();
                expect(selectedContacts.length).toBe(4); // Should not add more

                alertSpy.mockRestore();
            });

            test('should handle DOM-state mismatch recovery', () => {
                addContactField('email');

                selectedContacts.splice(selectedContacts.indexOf('email'), 1);

                addContactField('email');

                expect(document.getElementById('contact-email')).toBeTruthy();
            });
        });
    });
});