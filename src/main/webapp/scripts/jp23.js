window.addEventListener('load', function () {
  console.log("JP23 Chatbot JS loaded!");

  const chatButton = document.createElement("div");
  chatButton.innerHTML = "💬";
  chatButton.style.cssText = `
    position: fixed; bottom: 20px; right: 20px;
    background-color: #0073e6; color: white;
    font-size: 20px; padding: 10px;
    border-radius: 50%; cursor: pointer;
    z-index: 9999;
  `;

  const chatWindow = document.createElement("div");
  chatWindow.style.cssText = `
    position: fixed; bottom: 70px; right: 20px;
    width: 300px; height: 400px; background: white;
    border: 1px solid #ccc; border-radius: 8px;
    padding: 10px; display: none;
    flex-direction: column; z-index: 9999;
    box-shadow: 0 2px 10px rgba(0,0,0,0.2);
  `;

  chatWindow.innerHTML = `
    <div style="flex:1; overflow-y:auto;" id="chat-log"></div>
    <input type="text" id="chat-input" placeholder="Ask something..." style="width: 100%; margin-top: 10px;" />
  `;

  chatButton.onclick = () => {
    chatWindow.style.display = chatWindow.style.display === "none" ? "flex" : "none";
  };

  document.body.appendChild(chatButton);
  document.body.appendChild(chatWindow);

  document.getElementById("chat-input").addEventListener("keypress", function (e) {
    if (e.key === "Enter") {
      const msg = this.value;
      if (!msg.trim()) return;
      const log = document.getElementById("chat-log");
      log.innerHTML += "<div><b>You:</b> " + msg + "</div>";
      fetch("/jenkins/chatbot/chat", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "message=" + encodeURIComponent(msg)
      })
      .then(r => r.text())
      .then(reply => {
        log.innerHTML += "<div><b>Bot:</b> " + reply + "</div>";
        log.scrollTop = log.scrollHeight;
      });
      this.value = "";
    }
  });
});
