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


class Telegram_api:
    def __init__(self, name, api_hash, api_id):
        self.MY_NAME = name
        self.LIMIT = 1
        self.path_photo_2 = 'photo/'
        self.client = TelegramClient(name, api_id, api_hash)
        self.client.connect()
        self.client.start()


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
        # path_photo = await client.download_profile_photo(chan_ent,path_photo_2)
        # link_on_photo = await upload_get_link(path_photo)
        # await fullDB_Dia(ch_id, title)
        # await fullDB_Photo(ch_id,link_on_photo)
        print("ID:", dia.message.peer_id.channel_id)
        print("TITLE: ", dia.name)
        print("DIA: ", dia)
    
    async def get_all_channels(self):
        await self.client.send_message('me', 'Thanks for the Telethon library!')
        dialog_arr = await self.take_dialog()
        data = []
        iteration = 0
        for dialog in dialog_arr:
            if dialog.name != "Enekin":
                print(dialog.name)
                try:
                    data.append({"id":dialog.message.peer_id.channel_id,"name":dialog.name})
                except BaseException:
                    print(dialog.message)
        return data

    # clientDB = MongoClient('localhost', 27017)
    # db = clientDB.test_database
    # db = clientDB['test']
    # collection = db['test']
    # posts = db.stage
    # #
    # create_Mes_T = """CREATE TABLE IF NOT EXISTS Message_T(
    #                 #ID serial PRIMARY KEY,
    #                 #CHANNEL_ID varchar (20) NOT NULL,
    #                 #TEXT varchar(20000),
    #                 #DATE TIMESTAMP,
    #                 #FWD_FROM VARCHAR(20),
    #                 #MENTION_ID VARCHAR(20),
    #                 #FORWARDS INTEGER,
    #                 #VIEWS INTEGER,
    #                 #IDOFMSG INTEGER);
    #                 #"""
    # create_Dialog_T = """CREATE TABLE IF NOT EXISTS Dialog_T(
    #                 #ID serial PRIMARY KEY,
    #                 #CHANNEL_ID varchar (20) NOT NULL ,
    #                 #TITLE varchar(500));
    #                 #"""
    #
    # create_Dialog_Ph = """CREATE TABLE IF NOT EXISTS Dialog_Ph(
    #                         #CHANNEL_ID VARCHAR(20),
    #                         #LINK_PHOTO VARCHAR(50))"""
    #

    # Listen to messages from target channel
    # @client.on(events.NewMessage("me"))

    async def getMessagesDB(self):
        dialog_arr = self.take_dialog()
        for i in range(2, len(dialog_arr) - 1):
            await self.dialog_info(dialog_arr[i])
            async for message in self.client.iter_messages(dialog_arr[i], limit=self.LIMIT):
                await self.takeMesInfo(message)
            # try:
            #     with self.client.takeout() as takeout:
            #         for message in takeout.iter_messages(dialog_arr[i], wait_time=0):
            #             ...  # Do something with the message
            #
            # except errors.TakeoutInitDelayError as e:
            #     print('Must wait', e.seconds, 'before takeout')


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
        # await full_mong(message.date,author_id,message.text)
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

    # [{name,id},{name,id}]

