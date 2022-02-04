from tornado.web import Application, RequestHandler
from tornado.ioloop import IOLoop
from telegram import Telegram_api
from tgconfig import API_HASH, API_ID
import json

SELF_NAME = "Enekin"
telegram = Telegram_api(SELF_NAME, API_HASH, API_ID)
# telegram.write_me()

class HelloHandler(RequestHandler):
    async def get(self):
        print("get")
        res = await telegram.get_all_channels()
        jsonString = json.dumps(res)
        self.write(jsonString)

def make_app():
    urls = [("/dialogs", HelloHandler)]
    return Application(urls)


if __name__ == '__main__':
    app = make_app()
    app.listen(3000)
    IOLoop.instance().start()