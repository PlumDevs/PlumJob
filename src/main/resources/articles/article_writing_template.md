## How to add new article for the Tips module? ##

Every article is parsed from a .txt file in this directory by the ArticleService, responsible for both creating a thumbnail and communicating with ArticleView to display the chosen article in full.

### Format of the .txt file ###
__1st line:__ Title <br>
__2nd line:__ First and last name of the author <br>
__3rd line:__ Subtitle <br>

Next lines are according to the following pattern (k is an integer greater than 1): <br>

__(2k)th line:__ Pointed paragraph title, ie. 1. Do your best <br>
__(2k + 1)th line:__ Paragraph of text <br>
... <br>
__Last line__ of the article: Summary

__Examples of articles written according to this template can be found in behavioural.txt, interviews.txt, jobhunt.txt, resume.txt, portfolio.txt__

### Placing the article on website ###

1. Add your .txt file to _PlumJob/src/main/resources/articles/_ to be read correctly.
2. Add ``` add(articleService.createArticleThumbnail(<File name without extension>, <Displayed title>)); ``` at the end of the ArticlesView.java constructor.
3. Run the app and test if your article is opening correctly.
