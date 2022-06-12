from tornado.web import Application, RequestHandler
from tornado.ioloop import IOLoop
from telegram import Telegram_api
from tgconfig import API_HASH, API_ID
import json

SELF_NAME = "Enekin"
telegram = Telegram_api(SELF_NAME, API_HASH, API_ID)

class GetChannels(RequestHandler):
    async def get(self):
        print("get")
        res = await telegram.get_all_channels()
        jsonString = json.dumps(res)
        self.write(jsonString)

class JoinChannel(RequestHandler):
    async def get(self):
        print("get")
        name = self.get_argument('link')
        print(name)
        result = await telegram.join_channel(name)
        print(result)

class GetFolders(RequestHandler):
    async def get(self):
        print("get")
        res = await telegram.get_folders()
        jsonString = json.dumps(res)
        self.write(jsonString)

class GetCategories(RequestHandler):
    async def get(self):
        print("get")
        res = await telegram.get_categories()
        jsonString = json.dumps(res)
        self.write(jsonString)

class GetCategoriesSource(RequestHandler):
    async def get(self):
        print("get")
        res = await telegram.get_categories_channel()
        jsonString = json.dumps(res)
        self.write(jsonString)

class ProcessChannels(RequestHandler):
    async def get(self):
        print("proc")
        arr_channels = self.get_argument('sourceList')
        from_date = self.get_argument('startDate')
        to_date = self.get_argument('endDate')
        stop_w = self.get_argument('stop')
        sym_num = self.get_argument('symbolNum')
        range_day = self.get_argument('range')
        print(arr_channels.replace("[","").replace("]","").split(", "))
        print(from_date)
        print(to_date)
        print(stop_w)
        print(sym_num)
        print(range_day)
        # result = await telegram.join_channel(name)
    
def make_app():
    urls = [("/dialogs", GetChannels),
            ("/add", JoinChannel),
            ("/process", ProcessChannels),
            ("/categories", GetCategories),
            ("/folders", GetFolders)]

    return Application(urls)

if __name__ == '__main__':
    app = make_app()
    app.listen(3000)
    IOLoop.instance().start()