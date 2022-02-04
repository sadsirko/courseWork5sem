import asyncio

from flask import Flask, jsonify
from flask_restful import Api
from telegram import Telegram_api
from tgconfig import API_HASH, API_ID
import threading

SELF_NAME = "Enekin"
app = Flask(__name__)
api = Api(app)
telegram = Telegram_api(SELF_NAME, API_HASH, API_ID)
telegram.write_me()

# telegram = Telegram_api(SELF_NAME, API_HASH, API_ID)
# dialogs = telegram.get_dial()


def hello():
    return telegram.get_dial()


@app.route("/test", methods=["GET"])
def index():
    print(f"Inside flask function: {threading.current_thread().name}")
    asyncio.set_event_loop(asyncio.new_event_loop())
    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(hello())
    return jsonify({"result": result})


@app.route('/get/all', methods=['GET'])
async def get_channels():
    # loop = asyncio.get_event_loop()
    # result = loop.run_until_complete()
    result = telegram.take_dialog()
    return result


# async def main():


# asyncio.run(main())
if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', port=5000)

# with telegram.client:
#     telegram.client.run_until_disconnected()
with telegram.client:
    telegram.client.run_until_complete(get_channels)