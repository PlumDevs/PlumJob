const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const generatePDF = require('./pdf-service');

const app = express();
const PORT = 3001;

app.use(cors());
app.use(bodyParser.json({ limit: '10mb' }));

app.post('/generate', async (req, res) => {
    const { html, styles } = req.body;

    if (!html) {
        return res.status(400).json({ error: 'No HTML data' });
    }

    try {
        const pdfBuffer = await generatePDF(html, styles);

        res.set({
            'Content-Type': 'application/pdf',
            'Content-Disposition': 'attachment; filename=CV.pdf',
            'Content-Length': pdfBuffer.length,
            'Cache-Control': 'no-cache'
        });

        res.send(pdfBuffer);
    } catch (error) {
        console.error('PDF generation error:', error);
        res.status(500).json({
            error: 'Server error while generating PDF',
            details: error.message
        });
    }
});

app.get('/health', (req, res) => {
    res.json({ status: 'OK', message: 'PDF server is working fine' });
});

app.listen(PORT, () => {
    console.log(`The server is running on http://localhost:${PORT}`);
});

module.exports = app;
