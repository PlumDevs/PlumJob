<div align="center">
  <img src="https://github.com/user-attachments/assets/4adc9313-c87a-43c6-b590-8c3c67d20934" width="260" height="200" ><br>
  <i>Get that job. Ditch the stress.</i>
</div>

## 📋 Table of contents
+ [About](#about)
+ [Installation instructions](#getting-started)
+ [Contributors](#contributors)

## About
Have you ever felt like looking for a job is more stressful than the actual work?

Plum Job is a web platform designed to make the job-hunting experience for young seekers more organized and less frustrating. 

_According to multiple dictionaries, plum job means a __highly desirable job__ or position, often characterised by excellent pay, benefits, working conditions, and opportunities for advancement. Which is exactly the type of job we strive for our users to find in these uncertain times._

### Features 
+ Active and past __application archive__ with real-time updatable statuses, 
+ __CV builder__ that transforms user data into a clean, ATS-friendly PDF using one of our beautiful templates,
+ __Blog section__ filled with tips and insights from seasoned recruiters,
+ __Flow diagram generation__ based on the application archive, summarising the user’s job-seeking journey,
+ __Calendar__ providing e-mail notifications one hour prior to recorded events, like job interviews or online assessment deadlines,
+ __Targeted__ job offer __ads__ according to the preferences set in the user profile.

*As per today, some of the features are aimed mostly at IT-related jobs; however, we would love to expand to other industries' needs in the future!

### Built with
[![My Skills](https://skillicons.dev/icons?i=java,spring,mysql,idea,figma,aws,js&theme=light)](https://skillicons.dev)<img src="https://avatars.githubusercontent.com/u/1171922?v=4" width="55" height="55" />


## Project key structure
```
plumjob/
├── src/
│   ├── main/
│   │   ├── java/...
│   │   └── resources/
│   └── test/
├── .env
├── pom.xml
└── README.md
```

## Getting started
### Prerequisites
+ Java 17, JDK
+ Maven 3.8+
+ Stable internet connection (to access the online database)
+ *Node.js - instructions on getting everything necessary from node are below.

### Installation A - run deployed demo (recommended)
To showcase our project, we deployed it using AWS here: [http://ec2-13-51-60-227.eu-north-1.compute.amazonaws.com:9090/](http://ec2-13-51-60-227.eu-north-1.compute.amazonaws.com:9090/)

Important: to use our custom CV Builder in the deployed version (to be precise, to successfully download .pdf versions of created resumes), you still need to do these steps:

1. Download folder ``` Demo (executable) ```.
2. Open terminal in ``` Demo (executable)/puppeteer-pdf ``` and run
```
npm install express body-parser cors puppeteer
node pdf-server.js
```
This should output: ```The server is running on http://localhost:3001``` and it has to be running for the whole time of using the app.

3. Open in web browser http://ec2-13-51-60-227.eu-north-1.compute.amazonaws.com:9090/

### Installation B - run from .jar file
1. Download folder ``` Demo (executable) ```.
2. Open terminal in ``` Demo (executable)/puppeteer-pdf ``` and run
```
npm install express body-parser cors puppeteer
node pdf-server.js
```
This should output: ```The server is running on http://localhost:3001``` and it has to be running for the whole time of using the app.
If any issues occur with this or pdf generation functionality, please contact us.

3. Open terminal in Demo (executable) and run
```
java -jar plumjob-0.0.1-SNAPSHOT.jar
```
The complete app should be accessible via any web browser on [localhost:9090/](http://localhost:9090/)

## Tests
More in the Test Documentation files.

## Contributors
👩🏻‍💻[Kinga Żmuda](https://github.com/kingazm) - Project Manager and Software Engineer <br>
👨🏼‍💻[Dominik Szymczyk](https://github.com/tytusszymczyk) - Tester <br>
👩🏼‍💻[Martyna Cios](https://github.com/cssma) - Software Engineer <br>
👨🏻‍💻[Wojciech Popiel](https://github.com/PopielWojciech) - Database Engineer and Documentation Lead <br>
👨🏻‍💻[Piotr Szulej](https://github.com/PiterParker32) - Software Engineer

### Acknowledgements
[Skill Icons](https://github.com/tandpfun/skill-icons) <br>
[Google Chart - Sankey Diagram](https://developers.google.com/chart/interactive/docs/gallery/sankey?hl=pl)
