package com.project4.FilterPlayGround;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class DictionaryAccess {
  private static IDictionary dictionary;
  private static DictionaryAccess instance;

  private DictionaryAccess(String dPath) {
    try {
      dictionary = new Dictionary(new URL("file", null, dPath));
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      dictionary.open();
      instance = this;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void instanciate(String path) {
    instance = new DictionaryAccess(path);
  }

  public static DictionaryAccess getInstance() {
    return instance;
  }

  public List<String> getWordSenses(String name) {
    List<String> ret = new ArrayList<String>();
    IIndexWord idxWord = dictionary.getIndexWord(name, POS.NOUN);
    if (idxWord == null) return ret;
    if (idxWord.getWordIDs() == null) return ret;
    IWordID wordID = idxWord.getWordIDs().get(0);
    IWord word = dictionary.getWord(wordID);
    ISynset synset = word.getSynset();
    for (IWord w : synset.getWords())
      if (!w.getLemma().equals(name)) ret.add(w.getLemma());
    return ret;
  }
}
