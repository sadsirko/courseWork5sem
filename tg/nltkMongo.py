import nltk
import re
from stopwords_ua_list import stop_ua
from stop_words_russian import stop_ru
import rulemma
from pymystem3 import Mystem
import pymongo
from pymongo import MongoClient
import datetime
import json
from sqlalchemy import *
import random

engine = create_engine('postgresql://postgres:3793180d@localhost/postgres')
connection = engine.connect()

client = MongoClient()
client = MongoClient('localhost', 27017)
db = client.test_database
db = client['test']
collection = db['test']
posts = db.stage

lemmatizer = rulemma.Lemmatizer()
lemmatizer.load()
m = Mystem()

class Text:
    def __init__(self,text):
        self.text = text
        self.lemmas=[]
        self.ua="ukr"
        self.rus="rus"
        self.tokenized=[]
        self.ukr_arr=["і","є","ї"]
        self.rus_arr=["э","ё","ы","ъ"]
        self.getRes()
        
    def cutMain(self):
        if(len(self.text)>650):
            self.text = self.text[0:649]

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
        self.final = [word for word in self.lemmas if not word in stop_ru]

    def clearSW_ua(self):
        self.final = [word for word in self.tokenized if not word in stop_ua]

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

def add_tokenize():
    cursor=posts.find({})
    for document in cursor:
        _id = document["_id"]
        for ms in document["messages"]:
            if ms is None :
                posts.update_many({"_id":_id},{'$push':{'tokeized':[]}}, upsert = True)
            else:
                posts.update_many({"_id":_id},{'$push':{'tokeized':Text(ms).final}}, upsert = True)

def show_pared(ret):
    pares=[]
    res = {}
    cursor=posts.find({})
    for document in cursor:
        _id = document["_id"]
        if(_id != "20210621"):
            len_doc=len(document["tokeized"])
            for i in range(0,len_doc):
                for j in range(i + 1,len_doc):
                    general = set(document["tokeized"][i])&set(document["tokeized"][j])
                    if len(general ) > 2:
                        if(document["messages"][i] != len(document["messages"][j])):
                            if str(document["channels"][i]) != str(document["channels"][j]):
                                dd = str(document["channels"][i]) +"A"+ str(document["channels"][j])
                                pares.append([document["messages"][i],document["messages"][j]])
                                if not res.get(dd):
                                    res[dd] = 1
                                else:
                                    res[dd] += 1

    print(len(pares))
    if(ret == "db"):
        return res
    return pares

#add_tokenize()

def open_file(file_name,content):
    with open(file_name,"w") as text:
        _len = len(content)
        for i in range(0,20):
            text.write(str(content[random.randint(0,_len - 1)]))
            text.write('\n')
#change to db to get what we send
open_file("new.txt",show_pared("test"))

def write_to_postgr(dict_sum):
    insert_to_sum_t_2 = """
    insert into sum3_table(id1,id2,_count) VALUES(%s,%s,%s)
    """
    create_tb="""CREATE TABLE IF NOT EXISTS sum3_table(id1 varchar(20),id2 varchar(20),_count int);"""
    connection.execute(create_tb)
    for i in dict_sum.keys():
        tmp = dict_sum[i]
        pos = i.find("A")
        connection.execute(insert_to_sum_t_2,i[0:pos],i[pos + 1:len(i)],tmp)

#write_to_postgr(show_pared())

#print(set(ex1)&set(ex3))
