//로그인 시스템 대시 임시방편으로 채팅 룸 번호를 설정한다.
let username = prompt("아이디를 입력하세요");
let roomNum = prompt("채팅방 번호를 입력하세요");

document.querySelector("#username").innerHTML = username;

const eventSource = new EventSource(`http://localhost:9090/chat/roomNum/${roomNum}`);

eventSource.onmessage = (event) => { //event가 걸려있기 때문에 data 들어올 때 마다 박스 업데이트를 한다
    // console.log(1, event);
    // const data = JSON.parse(event.data);
    // console.log(2, data);
    const data = JSON.parse(event.data);
    console.log(data);
    if (data.sender === username) {
        initMyMessage(data); //파란박스(오른쪽)
    } else {
        initYourMessage(data);//회색박스(왼쪽)
    }
}

function getSendMsgBox(data) {
    return `<div class="sent_msg">
    <p>${data.msg}</p>
    <span class="time_date"> ${data.createdTime} | ${data.sender}</span>
    </div>`;
}

function getReceiveMsgBox(data) {
    return `<div class="received_withd_msg">
    <p>${data.msg}</p>
    <span class="time_date"> ${data.createdTime} / ${data.sender} </span>
    </div>`;
}

function initMyMessage(data) {

    const parsedDate = new Date(data.createdTime);
    const month = parsedDate.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줌
    const day = parsedDate.getDate();
    const formattedTime = `${month}/${day}`;

    let chatBox = document.querySelector("#chat-box");


    let sendBox = document.createElement("div");
    sendBox.className = "outgoing_msg";

    sendBox.innerHTML = getSendMsgBox(data);
    chatBox.append(sendBox);

    
	document.documentElement.scrollTop = document.body.scrollHeight;
}

function initYourMessage(data) {

    const parsedDate = new Date(data.createdTime);
    const month = parsedDate.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줌
    const day = parsedDate.getDate();
    const formattedTime = `${month}/${day}`;

    let chatBox = document.querySelector("#chat-box");


    let receivedBox = document.createElement("div");
    receivedBox.className = "received_msg";

    receivedBox.innerHTML = getReceiveMsgBox(data);
    chatBox.append(receivedBox);

    
	document.documentElement.scrollTop = document.body.scrollHeight;
}



// addMessage() 함수 호출시 DB에 insert되고 그 데이터가 자동으로 흘러들어온다.(SSE 프로토콜 SERVER SENT EVENT)
async function addMessage() {
    let chatBox = document.querySelector("#chat-box");
    let msgInput = document.querySelector("#chat-outgoing-msg");

    let chatOutgoingBox = document.createElement("div");
    chatOutgoingBox.className = "outgoing_msg";

    let date = new Date();
    let now = date.getHours() + ":" + date.getMinutes();

    let chat = {
        sender: username,
        roomNum: roomNum,
        msg: msgInput.value
    };

    fetch("http://localhost:9090/chat", {
        method: "post", //http post 메서드
        body: JSON.stringify(chat), //JS -> JSON
        headers: {
            "Content-Type": "application/json; charset=utf-8"
        }
    });

    // console.log(response);
    // let parseResponse = await response.json();
    //  console.log(parseResponse);

    msgInput.value = "";

}

document.querySelector("#chat-outgoing-button").addEventListener("click", () => {
    addMessage();
});

// 엔터를 치면 메시지 전송
document.querySelector("#chat-outgoing-msg").addEventListener("keydown", (e) => {
    if (e.keyCode === 13) {
        addMessage();
    }
});