const puppeteer = require('puppeteer');

async function generatePDF(html, additionalStyles = '') {
    const browser = await puppeteer.launch({
        headless: 'new',
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox'
        ]
    });

    const page = await browser.newPage();
    await page.setViewport({ width: 1200, height: 1600 });

    const htmlWithFonts = `
      <!DOCTYPE html>
      <html>
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Libre+Caslon+Text:ital,wght@0,400;0,700;1,400&display=block" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" crossorigin="anonymous" />

        <style>
          @import url('https://fonts.googleapis.com/css2?family=Libre+Caslon+Text:ital,wght@0,400;0,700;1,400&display=swap');

          * {
            box-sizing: border-box;
            -webkit-print-color-adjust: exact !important;
            print-color-adjust: exact !important;
          }

          body {
            font-family: 'Libre Caslon Text', 'Times New Roman', serif !important;
            margin: 0;
            padding: 0;
            background: white;
            font-size: 14px;
            line-height: 1.6;
          }

          h1, h2, h3 {
            font-weight: 700;
          }

          .fas, .far, .fab, .fa {
            font-family: "Font Awesome 5 Free" !important;
            font-weight: 900 !important;
            display: inline-block;
          }

          .fab {
            font-family: "Font Awesome 5 Brands" !important;
            font-weight: 400 !important;
          }

          .far {
            font-weight: 400 !important;
          }

          .fa-envelope::before { content: "✉"; }
          .fa-phone::before { content: "📞"; }
          .fa-home::before { content: "🏠"; }
          .fa-linkedin::before { content: "💼"; }
          .fa-github::before { content: "🔗"; }

          .add-button, .delete-button, .contact-options, #toggle-edit, #generate-pdf-btn {
            display: none !important;
          }

          #contact-fields {
            display: flex !important;
            flex-wrap: wrap !important;
            gap: 10px !important;
            align-items: center !important;
            justify-content: center !important;
            margin-bottom: 10px;
            flex-direction: row !important; 
          }

          .contact-field {
            display: flex !important;
            align-items: center !important;
            gap: 6px !important;
            font-size: 0.85rem;
            color: #444;
            flex-direction: row !important; 
          }

          .contact-fields {
            display: flex !important;
            gap: 4px !important;
            justify-content: center !important;
            align-items: center !important;
            flex-direction: row !important; 
          }

          ${additionalStyles}
        </style>
      </head>
      <body>
        ${html}
      </body>
      </html>
    `;

    try {
        await page.setContent(htmlWithFonts, {
            waitUntil: ['networkidle0', 'domcontentloaded'],
            timeout: 90000
        });

        await page.evaluate(() => document.fonts.ready);

        await page.waitForTimeout(1000);

        const pdfBuffer = await page.pdf({
            format: 'A4',
            printBackground: true,
            margin: { top: '20mm', bottom: '20mm', left: '10mm', right: '10mm' }
        });

        await browser.close();
        return pdfBuffer;

    } catch (error) {
        console.error('Error in generating PDF:', error);
        await browser.close();
        throw error;
    }
}

module.exports = generatePDF;