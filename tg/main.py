import configparser
import json
import re
from telethon.errors import SessionPasswordNeededError
from telethon import TelegramClient, events, sync
from telethon.tl.functions.messages import (GetHistoryRequest)
from telethon.tl.types import (PeerChannel)
import asyncio
from re import search
from sqlalchemy import *
# import pyodbc
import os
import subprocess
from time import sleep
from telethon.tl.functions.channels import JoinChannelRequest
import pymongo
from pymongo import MongoClient
import datetime

LIMIT = 1
api_hash = '00a76516fe994dc0739be7ceca82583a'
api_id = 5187921
# Here you define the target channel that you want to listen to:
#user_input_channel = YOUR_TRIGGER_CHANNEL

# engine = create_engine('postgresql://postgres:3793180d@localhost/postgres')
path_photo_2='photo/'
# connection = engine.connect()
client = TelegramClient('anon', api_id, api_hash)

clientDB = MongoClient('localhost', 27017)
db = clientDB.test_database
db = clientDB['test']
collection = db['test']
posts = db.stage
#
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
client.start()
# @client.on(events.NewMessage("me"))
@client.on(events.ChatAction("me") )
async def ConnectionListener(event):
    print("asd")
    # await getMessagesDB()

async def getMessagesDB():
    print("get alive")
    dialog_arr = await take_dialog()
    for i in range(2, len(dialog_arr) - 1):
        await dialog_info(dialog_arr[i])
        async for message in client.iter_messages(dialog_arr[i], limit = LIMIT):
            await takeMesInfo(message)

async def take_dialog():
    print("get alive")
    dialogs = await client.get_dialogs()
    return dialogs

# async def create_table():
#     connection.execute(create_Dialog_T)
#     connection.execute(create_Mes_T)
#     connection.execute(create_Dialog_Ph)

#photo downloadin disabled

async def dialog_info(dia):
    ch_id = dia.message.peer_id.channel_id
    title = dia.name
    chan_ent = await client.get_entity(PeerChannel(ch_id))
    # path_photo = await client.download_profile_photo(chan_ent,path_photo_2)
    # link_on_photo = await upload_get_link(path_photo)
    # await fullDB_Dia(ch_id, title)
    #await fullDB_Photo(ch_id,link_on_photo)
    print("ID:",dia.message.peer_id.channel_id)
    print("TITLE: ",dia.name)
    print("DIA: ",dia)

async def takeMesInfo(message):
    author_id = message.peer_id.channel_id
    mess = message.text
    fwd_count = message.forwards
    views_count = message.views
    id_of_msg = message.id
    from_id = await whose(message)
    mention_id = await mention(message)
    date = message.date
    # await fullDB_Msg(author_id,mess,date,from_id,mention_id,views_count,fwd_count,id_of_msg)
    # await full_mong(message.date,author_id,message.text)
    print("author_id:",author_id)
    print("Message :",message.text)
    print("Mess from: ",await whose(message))# id or 'null'
    print("Mention: ",await mention(message))# chanelid or 'null'
    print("DATE: ",message.date)

async def whose(message):
    try:
        return(message.fwd_from.from_id.channel_id)
    except AttributeError:
        return('null')


async def mention(message):
    try:
        link = message.media.webpage.url
        if(await isItTg(link)):
            return(await getChannelByPost(message.media.webpage.url))
    except :
        return('null')


async def cutLink(link):
    new_link = search('(.*?)/[0-9]+$', link).group(1)
    return new_link

async def getChannelByPost(link):
    slash = '/'
    count_of_slash = link.count(slash)
    res = link
    if link.count(slash) == 4:
        res = await client.get_entity(await cutLink(link))
    return res.id

async def isItTg(link):
    if(link[8:12] == "t.me"):
        return True
    else:
        return False

# async def fullDB_Msg(author_id,mess,date,from_id,mention_id,views_count,fwd_count,id_of_msg):
#     insert_to_mes = """
#     insert into Message_T(CHANNEL_ID,TEXT,DATE,FWD_FROM,MENTION_ID,VIEWS,FORWARDS,IDOFMSG) VALUES(%s,%s,%s,%s,%s,%s,%s,%s)
#     """
#     connection.execute(insert_to_mes,author_id,mess,date,from_id,mention_id,views_count,fwd_count,id_of_msg)
#
# async def fullDB_Dia(title,id_):
#     insert_to_mes = """
#     insert into Dialog_T(CHANNEL_ID,TITLE) VALUES(%s,%s)
#     """
#     connection.execute(insert_to_mes,title,id_)
#
# async def fullDB_Photo(id_,photo_link):
#     insert_to_mes = """
#     insert into Dialog_Ph(CHANNEL_ID,LINK_PHOTO) VALUES(%s,%s)
#     """
#     connection.execute(insert_to_mes,id_,photo_link)

# async def upload_get_link(photo):
#     sleep(11)
#     result = subprocess.run(['imgur-uploader',photo],stdout=subprocess.PIPE,encoding='utf-8')
#     return(search("(?P<url>https?://[^\s]+)", result.stdout).group("url"))
#
# async def full_mong(date,chanel_id,message):
#     _id=str(date)[0:4]+str(date)[5:7]+str(date)[8:10]
#     posts.update_many({"_id":_id},{'$push':{'messages':message}}, upsert = True)
#     posts.update_many({"_id":_id},{'$push':{'channels':chanel_id}}, upsert = True)

# connection.close()
#
# asyncio.run(client.send_message('me', 'Hi'))

if __name__ == "__main__":
    for message in client.iter_messages('me',limit = 2):
        print(message)
    sleep(10)
    client.send_message('me', 'Hi')

with client:
    client.run_until_disconnected()
