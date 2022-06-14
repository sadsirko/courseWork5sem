import nltk
import re
from stopwords_ua_list import stop_ua
from stopwords_ru_list import stop_ru
from stopwords_additional import stop_add

import rulemma
from pymystem3 import Mystem
import pymongo
from pymongo import MongoClient
import datetime
import json
from sqlalchemy import *
import random
from bson.json_util import dumps
from os.path import join
import pandas as pd
from pymongo import MongoClient

engine = create_engine('postgresql://postgres:3793180d@localhost/postgres')
connection = engine.connect()

client = MongoClient()
client = MongoClient('localhost', 27017)
db = client['test']
collection = db['test']
posts = db.stage

lemmatizer = rulemma.Lemmatizer()
lemmatizer.load()
m = Mystem()

class Text:
    def __init__(self,text,max_len):
        self.text = text
        self.max_len = int(max_len)
        self.lemmas=[]
        self.ua="ukr"
        self.rus="rus"
        self.tokenized=[]
        self.ukr_arr=["і","є","ї"]
        self.rus_arr=["э","ё","ы","ъ"]
        self.getRes()
        
    def cutMain(self):
        if(len(self.text)>self.max_len):
            self.text = self.text[0:self.max_len]

    def clearFromLink(self):
        self.text=re.sub(r"([@#][A-Za-z0-9]+)|([@#][А-Яа-я0-9]+)|(\w+:\/\/\S+)"," ",self.text)

    def clearFromSymb(self):
        self.text=re.sub(r"[-«»()\[\]\"’—#/@;:<>{}=~|.?!,⚡️0-9#№$*]|\\n", " ", self.text)

    def tokenize(self):
            self.tokenized=nltk.word_tokenize(self.text.lower())

    def defLang(self):
        if len(self.tokenized)>0:
            for i in range(0,len(self.tokenized)-1):
                for j in self.tokenized[i]:
                    if j in self.ukr_arr:
                        self.lang= self.ua
                        return
                    if j in self.rus_arr:
                        self.lang= self.rus
                        return
        self.lang= self.rus
        return

    def lem(self):
        for word in self.tokenized:
            self.lemmas.append(m.lemmatize(word)[0])

    def clearSW_ru(self):
        self.final = [word for word in self.lemmas if not word in stop_ru + stop_add]

    def clearSW_ua(self):
        self.final = [word for word in self.tokenized if not word in stop_ua + stop_add]

    def getRes(self):
        self.cutMain()
        self.clearFromLink()
        self.clearFromSymb()
        self.tokenize()
        self.defLang()
        if self.lang == self.rus:
            self.lem()
            self.clearSW_ru()
        elif self.lang == self.ua:
            self.clearSW_ua()
        else:
            self.final=self.tokenized


def add_tokenize(post):
    cursor=post.find({})
    for document in cursor:
        _id = document["_id"]
        for ms in document["messages"]:
            if ms is None :
                posts.update_many({"_id":_id},{'$push':{'tokeized':[]}}, upsert = True)
            else:
                posts.update_many({"_id":_id},{'$push':{'tokeized':Text(ms,650).final}}, upsert = True)


def backup_db(backup_db_dir):
    client = pymongo.MongoClient(host='localhost', port=27017)
    database = client['test']
    # authenticated = database.authenticate(<uname>,<pwd>)
    # assert authenticated, "Could not authenticate to database!"
    collections = database.list_collection_names()
    for i, collection_name in enumerate(collections):
        col = getattr(database,collections[i])
        collection = col.find()
        jsonpath = collection_name + ".json"
        jsonpath = join(backup_db_dir, jsonpath)
        with open(jsonpath, "w", encoding="utf8") as jsonfile:
            jsonfile.write(dumps(collection))

backup_db("./")
# add_tokenize(posts)
