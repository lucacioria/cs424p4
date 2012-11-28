package com.project4.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import processing.core.PConstants;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.Config.MyFontEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;

public class TagCloud extends VizPanel implements TouchEnabled, EventSubscriber {

  private static final int GRID_X = 5, GRID_Y = 3;
  private static final int NUMBER_OF_WORDS_PER_GRID = 2;
  private static final int MAX_TEXT = 50, MIN_TEXT = 15;
  private TreeMap<Filter, ArrayList<Tweet>> tweets = new TreeMap<Filter, ArrayList<Tweet>>();
  private Map map;
  private TreeMap<String, WordPos>[][] wordsMap;
  private int maxCounts, minCounts;
  private ArrayList<WordPos>[][] wordPoses;
  private String[] bl = {"","&", "en", "de", "ur", "dont", "0", "9", "8", "7", "6", "5", "4", "3",
                         "2", "1", "im", "a", "a\'s", "able", "about", "above", "according", "accordingly", "across",
                         "actually", "after", "afterwards", "again", "against", "ain't", "all", "allow", "allows",
                         "almost", "alone", "along", "already", "also", "although", "always", "am", "among",
                         "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything",
                         "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are",
                         "aren't", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away",
                         "awfully", "b", "be", "became", "because", "become", "becomes", "becoming", "been", "before",
                         "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better",
                         "between", "beyond", "both", "brief", "but", "by", "c", "c'mon", "c's", "came", "can",
                         "can't", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly",
                         "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering",
                         "contain", "containing", "contains", "corresponding", "could", "couldn't", "course",
                         "currently", "d", "definitely", "described", "despite", "did", "didn't", "different", "do",
                         "does", "doesn't", "doing", "don't", "done", "down", "downwards", "during", "e", "each",
                         "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially",
                         "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere",
                         "ex", "exactly", "example", "except", "f", "far", "few", "fifth", "first", "five",
                         "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from",
                         "further", "furthermore", "g", "get", "gets", "getting", "given", "gives", "go", "goes",
                         "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn't", "happens", "hardly",
                         "has", "hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence",
                         "her", "here", "here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
                         "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i",
                         "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored", "immediate", "in", "inasmuch",
                         "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into",
                         "inward", "is", "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "j", "just",
                         "k", "keep", "keeps", "kept", "know", "knows", "known", "l", "last", "lately", "later",
                         "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely",
                         "little", "look", "looking", "looks", "ltd", "m", "mainly", "many", "may", "maybe", "me",
                         "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must",
                         "my", "myself", "n", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs",
                         "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none",
                         "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "o", "obviously",
                         "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto",
                         "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside",
                         "over", "overall", "own", "p", "particular", "particularly", "per", "perhaps", "placed",
                         "please", "plus", "possible", "presumably", "probably", "provides", "q", "que", "quite",
                         "qv", "r", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless",
                         "regards", "relatively", "respectively", "right", "s", "said", "same", "saw", "say",
                         "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming",
                         "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven",
                         "several", "shall", "she", "should", "shouldn't", "since", "six", "so", "some", "somebody",
                         "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon",
                         "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t",
                         "t's", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that",
                         "that's", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence",
                         "there", "there's", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon",
                         "these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this",
                         "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru",
                         "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly",
                         "try", "trying", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlikely",
                         "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually",
                         "uucp", "v", "value", "various", "very", "via", "viz", "vs", "w", "want", "wants", "was",
                         "wasn't", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well", "went",
                         "were", "weren't", "what", "what's", "whatever", "when", "whence", "whenever", "where",
                         "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever",
                         "whether", "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose",
                         "why", "will", "willing", "wish", "with", "within", "without", "won't", "wonder", "would",
                         "would", "wouldn't", "x", "y", "yes", "yet", "you", "you'd", "you'll", "you're",
                         "you've", "your", "yours", "yourself", "yourselves", "z", "zero"};

  public TagCloud(float x0, float y0, float width, float height, Map parent) {
    super(x0, y0, width, height, parent);
    this.map = parent;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return false;
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DATA_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.MAP_ZOOMED_OR_PANNED, this);
    setVisible(false);
    setupGrid();
  }

  private void setupGrid() {
    wordsMap = new TreeMap[GRID_Y][GRID_X];
    maxCounts = 0;
    minCounts = 0;
    for (int i = 0; i < GRID_X; i++) {
      for (int j = 0; j < GRID_Y; j++) {
        wordsMap[j][i] = new TreeMap<String, WordPos>();
      }
    }
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    textFont(MyFontEnum.HELVETICA);
    textAlign(PConstants.CENTER, PConstants.CENTER);
    if (wordPoses != null) drawWords();
    popStyle();
    return false;
  }

  // private void drawWords() {
  // PVector position;
  // for (int i = 0; i < GRID_X; i++) {
  // for (int j = 0; j < GRID_Y; j++) {
  // for (String s : wordsMap[j][i].keySet()) {
  // if (wordsMap[j][i].get(s).getCount() >= topCounts[j][i]) {
  // float size = Math.max(Math.min(wordsMap[j][i].get(s).getCount(), 30), 15);
  // textSize(size);
  // position = wordsMap[j][i].get(s).getPosition();
  // text(s, position.x, position.y);
  // }
  // }
  // }
  // }
  // }

  private void drawWords() {
    for (int j = 0; j < wordPoses.length; j++) {
      for (int k = 0; k < wordPoses[j].length; k++) {
        int count = 0;
        for (int i = 0; i < wordPoses[j][k].size() && i < NUMBER_OF_WORDS_PER_GRID; i++) {
          count++;
          computeRealPosition(j, k, i, count);
          float size = map(wordPoses[j][k].get(i).getCount(),maxCounts,minCounts,MAX_TEXT,MIN_TEXT);
          //float size = Math.max(Math.min(wordPoses[j][k].get(i).getCount(), 30), 15);
          textSize(size);
          fill(MyColorEnum.WHITE);
          text(wordPoses[j][k].get(i).word, wordPoses[j][k].get(i).getPosition().x, wordPoses[j][k].get(i).getPosition().y);
        }
      }
    }
  }

//  private void computeRealPosition(int j, int k, int i, int count) {
//    for (int l = 0; l < i; l++) {
//      int sizep1 = (int) map(wordPoses[j][k].get(i).getCount(),maxCounts,minCounts,MAX_TEXT,MIN_TEXT);;
//      int sizep2 = (int) map(wordPoses[j][k].get(l).getCount(),maxCounts,minCounts,MAX_TEXT,MIN_TEXT);
//      if (overlaps(wordPoses[j][k].get(i), wordPoses[j][k].get(l), sizep1, sizep2)) {
//        move(wordPoses[j][k].get(i),wordPoses[j][k].get(l),sizep1,sizep2,count);
//        log(wordPoses[j][k].get(i).word + "," + wordPoses[j][k].get(l).word + " "
//            + wordPoses[j][k].get(i).getPosition().y);
//        computeRealPosition(j, k, i, count);
//      }
//    }
//  }
//  
  private void computeRealPosition(int j, int k, int i, int count) {
    for (int g1 = 0; g1 <= j; g1++) {
      for (int g2 = 0; g2 < wordPoses[g1].length; g2++) {
        for (int l = 0; l < wordPoses[g1][g2].size(); l++) {
          if(g1==j&&g2==k&&l==i) continue;
          int sizep1 = (int) map(wordPoses[j][k].get(i).getCount(),maxCounts,minCounts,MAX_TEXT,MIN_TEXT);;
          int sizep2 = (int) map(wordPoses[g1][g2].get(l).getCount(),maxCounts,minCounts,MAX_TEXT,MIN_TEXT);
          if (overlaps(wordPoses[j][k].get(i), wordPoses[g1][g2].get(l), sizep1, sizep2)) {
            move(wordPoses[j][k].get(i),wordPoses[g1][g2].get(l),sizep1,sizep2,count);
            //log(wordPoses[j][k].get(i).word + "," + wordPoses[g1][g2].get(l).word + " "
            //    + wordPoses[j][k].get(i).getPosition().y);
            computeRealPosition(j, k, i, count);
          }
        }
      }
    }
  }
//  

  private void move(WordPos wordPos1,WordPos wordPos2,int sizep1,int sizep2,int count) {
    textSize(sizep1);
    float height1 = textAscent();
    float widthp1 = textWidth(wordPos1.word);
    textSize(sizep2);
    float height2 = textAscent();
    float widthp2 = textWidth(wordPos2.word);
    int sumHeights = (int) (height1/2 + height2/2);
    int distY = (int) (wordPos2.getPosition().y-wordPos1.getPosition().y);
    int sumWidths = (int) (widthp1/2 + widthp2/2);
    int distX = (int) (wordPos2.getPosition().x-wordPos1.getPosition().x);
    if(count % 4 == 0){
      if(distY>0)
        wordPos1.getPosition().y += (int) Math.abs(distY) - sumHeights - 1 ;
      else
        wordPos1.getPosition().y += (int) - Math.abs(distY) - sumHeights - 1 ;
    }
    else if(count % 4 == 1){
      if(distY>0) wordPos1.getPosition().y += Math.abs(distY) + sumHeights +1 ;
      else wordPos1.getPosition().y += - Math.abs(distY) + sumHeights +1;
    }
    else if(count % 4 == 2){
      if(distX>0)
        wordPos1.getPosition().x += Math.abs(distX) + sumWidths +1 ;
      else
        wordPos1.getPosition().x += - Math.abs(distX) + sumWidths +1;
    }
    else{
      if(distX>0)
        wordPos1.getPosition().x += Math.abs(distX) + sumWidths +1 ;
      else
        wordPos1.getPosition().x += - Math.abs(distX) + sumWidths +1;
    }

  }

  private boolean overlaps(WordPos wordPos1, WordPos wordPos2, int sizep1, int sizep2) {
    textSize(sizep1);
    float heightp1 = textAscent();
    float widthp1 = textWidth(wordPos1.word);
    textSize(sizep2);
    float heightp2 = textAscent();
    float widthp2 = textWidth(wordPos2.word);
    if(Math.abs(wordPos1.getPosition().y - wordPos2.getPosition().y) < (heightp1 / 2 + heightp2 / 2)
        && Math.abs(wordPos1.getPosition().x - wordPos2.getPosition().x) < (widthp1 / 2 + widthp2 / 2))
      return true;
    else return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (TreeMap<Filter, ArrayList<Tweet>>) data;
      updateData();
    }
    if (eventName == EventName.MAP_ZOOMED_OR_PANNED) {
      updateData();
    }
  }

  private void updateData() {
    Iterator<Filter> it = tweets.keySet().iterator();
    while (it.hasNext()) {
      Filter key = it.next();
      for (Tweet t : tweets.get(key)) {
        t.generateWords();
      }
    }
    generateWords();
    sortMap();
    computeMaxMin();
  }

  private void computeMaxMin() {
    findMin();
    findMax();
  }

  private void findMax() {
    maxCounts = Integer.MIN_VALUE;
    for (int j=0;j<wordsMap.length;j++){
      for (int i=0;i<wordsMap[j].length;i++){
        Set<String> words = wordsMap[j][i].keySet();
        for (String w: words){
          if(maxCounts<wordsMap[j][i].get(w).getCount())
            maxCounts = wordsMap[j][i].get(w).getCount();
        }
      }
    }
  }

  private void findMin() {
    minCounts = Integer.MAX_VALUE;
    for (int j=0;j<wordsMap.length;j++){
      for (int i=0;i<wordsMap[j].length;i++){
        Set<String> words = wordsMap[j][i].keySet();
        for (String w: words){
          if(minCounts>wordsMap[j][i].get(w).getCount())
            minCounts = wordsMap[j][i].get(w).getCount();
        }
      }
    }
  }

  private void sortMap() {
    wordPoses = new ArrayList[GRID_Y][GRID_X];
    for (int j = 0; j < wordPoses.length; j++) {
      for (int k = 0; k < wordPoses[j].length; k++) {
        wordPoses[j][k] = new ArrayList<WordPos>();
        for (String s : wordsMap[j][k].keySet())
          wordPoses[j][k].add(wordsMap[j][k].get(s));
        Collections.sort(wordPoses[j][k]);
        Collections.reverse(wordPoses[j][k]);
      }
    }


  }

  // private void updateData() {
  // Iterator<Filter> it = tweets.keySet().iterator();
  // int x,y;
  // String[] words;
  // PVector position;
  // while (it.hasNext()) {
  // Filter key = it.next();
  // for(Tweet t: tweets.get(key)){
  // x =
  // (int) ((t.getLon() - map.getMinLon()) / (map.getMaxLon() - map.getMinLon()) * GRID_X);
  // y =
  // (int) ((t.getLat() - map.getMinLat()) / (map.getMaxLat() - map.getMinLat()) * GRID_Y);
  //
  // words=t.getImportantWords().split(",");
  // position = map.getPositionByLatLon(t.getLat(), t.getLon());
  // for(String s: words){
  // if(!wordsMap[y][x].containsKey(s))
  // wordsMap[y][x].put(s, new WordPos(position));
  // wordsMap[y][x].get(s).add(position);
  // }
  // }
  // break;
  // }
  // for(int i=0;i<GRID_X;i++){
  // for(int j=0;j<GRID_Y;j++){
  // findTopCounts(j,i);
  // }
  // }
  // }

  private boolean isInBlackList(String word) {
    for (String w : bl)
      if (w.equals(word.toLowerCase().trim())) return true;
    return false;
  }

  private void generateWords() {
    Iterator<Filter> it = tweets.keySet().iterator();
    int x = 0, y = 0;
    setupGrid();
    while (it.hasNext()) {
      Filter key = it.next();
      for (Tweet t : tweets.get(key)) {
        if(map.isVisible()){
          x = (int) ((t.getLon() - map.getMinLon()) / (map.getMaxLon() - map.getMinLon()) * GRID_X);
          y = (int) ((t.getLat() - map.getMinLat()) / (map.getMaxLat() - map.getMinLat()) * GRID_Y);
          if (x<0 || x>=GRID_X || y<0 || y>=GRID_Y) continue;
              for (String w : t.words) {
            w = w.replaceAll("[^A-Za-z]", "");
            w = w.toLowerCase();
            if(isInBlackList(w)) continue;
            WordPos wp = wordsMap[y][x].get(w);
            PVector position = map.getPositionByLatLon(t.getLat(), t.getLon());
            if (wp == null) {
              wordsMap[y][x].put(w, new WordPos(position, w));
            } else {
              wp.addLocation(position);
            }
          }
        }
      }
    }
  }


  // private void findTopCounts(int j, int i) {
  // int threshold = 2;
  // ArrayList<Integer> counts = new ArrayList<Integer>();
  // for (String s : wordsMap[j][i].keySet())
  // counts.add(wordsMap[j][i].get(s).getCount());
  // Collections.sort(counts);
  // if (counts.size() > 3)
  // topCounts[j][i] = counts.get(counts.size() - threshold);
  // else
  // topCounts[j][i] = 1;
  // }



}
