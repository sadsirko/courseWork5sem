
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

import requests
from bs4 import BeautifulSoup



class Telegram_api:
    def __init__(self, name, api_hash, api_id):
        self.MY_NAME = name
        self.LIMIT = 1
        self.path_photo_2 = 'photo/'
        self.client = TelegramClient(name, api_id, api_hash)
        self.client.connect()
        self.client.start()
        self.clientDB = MongoClient('localhost', 27017)
        self.db = self.clientDB.test_database
        self.db = self.clientDB['test']
        self.collection = self.db['test']
        self.posts = self.db.stage

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

    async def get_folders(self):
        json = ''
        request = await self.client(functions.messages.GetDialogFiltersRequest())
        for dialog_filter in request:
            print(dialog_filter.title)
            for channel in dialog_filter.include_peers:
                json += "{\'" + str(dialog_filter.title)  + "\'," + "\'" + str(channel.channel_id) + "\'}," 
                print(channel.channel_id)
            json = json[:-1]
        print(json)
        return json

    async def get_categories(self):
        print(1)
        json = '{'
        request = await self.client(functions.messages.GetDialogFiltersRequest())
        for dialog_filter in request:
            json += "\'" + str(dialog_filter.title)  + "\'," 
            json += ','
            json = json[:-1]
        json = json[:-1]
        json += ','
        json += "}"
        print(json)
        return json

    async def getMessagesDB(self):
        dialog_arr = self.take_dialog()
        for i in range(2, len(dialog_arr) - 1):
            await self.dialog_info(dialog_arr[i])
            async for message in self.client.iter_messages(dialog_arr[i], limit=self.LIMIT):
                await self.takeMesInfo(message)

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
        await self.full_mong(message.date,author_id,message.text)
        print("author_id:", author_id)
        print("Message :", message.text)
        print("Mess from: ", await self.whose(message))  # id or 'null'
        print("Mention: ", await self.mention(message))  # chanelid or 'null'
        print("DATE: ", message.date)

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

