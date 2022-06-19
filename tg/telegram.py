
import configparser
import json
import re
from telethon.errors import SessionPasswordNeededError
from telethon import TelegramClient, events, sync
from telethon.tl.functions.messages import (GetHistoryRequest)
from telethon.tl.types import (PeerChannel)
from telethon import errors
import asyncio
from re import search
from sqlalchemy import *
# import pyodbc
import os
import subprocess
from time import sleep
from telethon.tl.functions.channels import JoinChannelRequest
from telethon import functions, types
import pymongo
from pymongo import MongoClient
import datetime
from datetime import datetime, timedelta
from nltkMongo import Text
from bson.json_util import dumps
from os.path import join
import pandas as pd

import requests
from bs4 import BeautifulSoup



class Telegram_api:
    def __init__(self, name, api_hash, api_id):
        self.MY_NAME = name
        self.LIMIT = 4
        self.path_photo_2 = 'photo/'
        self.client = TelegramClient(name, api_id, api_hash)
        self.client.connect()
        self.client.start()
        self.clientDB = MongoClient('localhost', 27017)
        self.db = self.clientDB.test_database
        self.db = self.clientDB['test']
        self.collection = self.db['test']
        self.posts = self.db.stage
        #linux problem
        self.time_offs = 3
    
    async def del_all(self):
        self.posts.drop()

    async def write_me(self):
        await self.client.send_message('me', "self.take_dialog()")

    def get_dial(self):
        return self.client.get_dialogs()

    async def join_channel(self,name):
        try:
            result = await self.client(functions.channels.JoinChannelRequest(
                channel = str(name)
            ))
            print(result.stringify())
        except ValueError:
            result = "Empty"
        return result

    async def get_channel(self, link):
        ch_ent = await self.client.get_entity(link)
        print(ch_ent)
        return ch_ent

    async def take_dialog(self):
        dialogs = await self.client.get_dialogs()
        return dialogs

    async def dialog_info(self, dia):
        ch_id = dia.message.peer_id.channel_id
        title = dia.name
        chan_ent = await self.client.get_entity(PeerChannel(ch_id))
        # await fullDB_Dia(ch_id, title)
        # await fullDB_Photo(ch_id,link_on_photo)
        print("ID:", dia.message.peer_id.channel_id)
        print("TITLE: ", dia.name)
        print("DIA: ", dia)

    #find images on page 
    async def find_link(self,url):
        response = requests.get(url)
        html_page = BeautifulSoup(response.text, 'html.parser')
        images = html_page.find_all("img")

        for index, image in enumerate(images):
            image_url= image.get("src")      #img src value
            return image_url


    async def get_all_channels(self):
        await self.client.send_message('me', 'Thanks for the Telethon library!')
        dialog_arr = await self.take_dialog()
        data = []
        iteration = 0
        for dialog in dialog_arr:
            if dialog.name != "Enekin":
                try:
                    link = "https://t.me/" + dialog.entity.username
                    data.append({"id":dialog.message.peer_id.channel_id,
                     "name":dialog.name,"link":link,
                     "photo": await self.find_link(link)
                     })
                except BaseException:
                    try:
                        data.append({"id":dialog.message.peer_id.channel_id,
                        "name":dialog.name,"link":"null"})
                    except BaseException:                
                        print(dialog.message)
        return data

    async def backup_db(self,backup_db_dir):
        client = pymongo.MongoClient(host='localhost', port=27017)
        database = client['test']
        # authenticated = database.authenticate(<uname>,<pwd>)
        # assert authenticated, "Could not authenticate to database!"
        collections = database.list_collection_names()
        print("backup")
        for i, collection_name in enumerate(collections):
            col = getattr(database,collections[i])
            collection = col.find()
            jsonpath = collection_name + ".json"
            jsonpath = join(backup_db_dir, jsonpath)
            with open(jsonpath, "w", encoding="utf8") as jsonfile:
                jsonfile.write(dumps(collection))
        print("done")

    async def add_end(self,data):
        file_object = open('stage.json', 'a')
        # Append 'hello' at the end of file
        file_object.write('\n' + data)
        # Close the file
        file_object.close()

    async def get_by_id(self,id_arr,from_date,to_date,stop_w,symbol_num,range_day):
        # print(stop_w)
        dialog_arr = await self.take_dialog()
        data = []
        from_date_time_obj = datetime.strptime(from_date, '%d/%m/%y %H:%M')
        to_date_time_obj = datetime.strptime(to_date, '%d/%m/%y %H:%M')
        print(id_arr,from_date,to_date_time_obj,from_date_time_obj)
        for dialog in dialog_arr:
            if dialog.name != "Enekin":
                try:
                    if id_arr.index(str(dialog.message.peer_id.channel_id)) >= 0:
                        async for message in self.client.iter_messages(dialog, limit = 2000):
                            datetime_obj = datetime.strptime(str(message.date)[0:19], '%Y-%m-%d %H:%M:%S') 
                            datetime_obj = datetime_obj + timedelta(hours = self.time_offs)
                            if (datetime_obj > from_date_time_obj) and (datetime_obj < to_date_time_obj):
                                await self.takeMesInfo(message)    
                except BaseException:         
                    pass
        await self.add_stopwords(stop_w)
        await self.nltk_use(symbol_num)
        return data
 
    async def get_by_folder(self,id):
        return await self.client.get_dialogs(folder=id)

    async def get_folders(self):
        data =[]
        json = ''
        request = await self.client(functions.messages.GetDialogFiltersRequest())
        for dialog_filter in request:
            print(dialog_filter.title)
            for channel in dialog_filter.include_peers:
                    data.append({"category":dialog_filter.title,
                     "source": channel.channel_id
                     })
        return data

    async def get_categories(self):
        print(1)
        data = []
        request = await self.client(functions.messages.GetDialogFiltersRequest())
        for dialog_filter in request:
                         data.append({"id":dialog_filter.id,
                     "name":dialog_filter.title})
        return data

    # async def getMessagesDB(self):
    #     dialog_arr = self.take_dialog()
    #     for i in range(2, len(dialog_arr) - 1):
    #         await self.dialog_info(dialog_arr[i])
    #         async for message in self.client.iter_messages(dialog_arr[i], limit=self.LIMIT):
    #             await self.takeMesInfo(message)

    async def takeMesInfo(self, message):
        author_id = message.peer_id.channel_id
        mess = message.text
        fwd_count = message.forwards
        views_count = message.views
        id_of_msg = message.id
        from_id = await self.whose(message)
        mention_id = await self.mention(message)
        date = message.date
        # await fullDB_Msg(author_id,mess,date,from_id,mention_id,views_count,fwd_count,id_of_msg)
        if len(message.text) > 0:
            await self.full_mong(message.date,author_id,message.text)
        # print("author_id:", author_id)
        # print("Message :", message.text)
        # print("Mess from: ", await self.whose(message))  # id or 'null'
        # print("Mention: ", await self.mention(message))  # chanelid or 'null'
        # print("DATE: ", message.date)

    async def add_stopwords(self,arr_words):
        f = open("stopwords_additional.py", "w")
        f.write("stop_add=" + str(arr_words).replace("\'","\""))
        f.close()
    
    async def nltk_use(self,range):
        cursor=self.posts.find({})
        for document in cursor:
            _id = document["_id"]
            for ms in document["messages"]:
                if ms is None :
                    self.posts.update_many({"_id":_id},{'$push':{'tokeized':[]}}, upsert = True)
                else:
                    self.posts.update_many({"_id":_id},{'$push':{'tokeized':Text(ms,range).final}}, upsert = True)


    async def whose(self, message):
        try:
            return (message.fwd_from.from_id.channel_id)
        except AttributeError:
            return ('null')

    async def mention(self, message):
        try:
            link = message.media.webpage.url
            if (await self.isItTg(link)):
                return (await self.getChannelByPost(message.media.webpage.url))
        except:
            return ('null')

    async def cutLink(self, link):
        new_link = search('(.*?)/[0-9]+$', link).group(1)
        return new_link

    async def getChannelByPost(self, link):
        slash = '/'
        count_of_slash = link.count(slash)
        res = link
        if link.count(slash) == 4:
            res = await self.client.get_entity(await self.cutLink(link))
        return res.id

    async def isItTg(self, link):
        if (link[8:12] == "t.me"):
            return True
        else:
            return False

    async def full_mong(self,date,chanel_id,message):
        _id=str(date)[0:4]+str(date)[5:7]+str(date)[8:10]
        self.posts.update_many({"_id":_id},{'$push':{'messages':message}}, upsert = True)
        self.posts.update_many({"_id":_id},{'$push':{'channels':chanel_id}}, upsert = True)

    # [{name,id},{name,id}]

