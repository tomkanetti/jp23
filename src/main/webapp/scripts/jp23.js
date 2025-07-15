let OPENAI_ENDPOINT = "";
let OPENAI_API_KEY = "";
let OPENAI_MODEL = "gpt-3.5-turbo";

fetch("/jenkins/jp23-settings/settings")
  .then(r => r.json())
  .then(data => {
    OPENAI_ENDPOINT = data.endpoint;
    OPENAI_API_KEY = data.apiKey;
    OPENAI_MODEL = data.model || "gpt-3.5-turbo";
  });

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

  const popup = document.createElement("div");
  popup.style.cssText = `
    position: fixed; top: 50%; left: 50%;
    transform: translate(-50%, -50%);
    width: 300px; background: white;
    border: 1px solid #ccc; border-radius: 8px;
    padding: 20px; display: none;
    z-index: 10000; box-shadow: 0 2px 10px rgba(0,0,0,0.2);
  `;
  popup.innerHTML = `
    <div style="margin-bottom: 10px;">Welcome to the Chatbot!</div>
    <button id="close-popup" style="background-color: #0073e6; color: white; border: none; padding: 10px; border-radius: 5px; cursor: pointer;">Close</button>
  `;

  document.body.appendChild(chatButton);
  document.body.appendChild(chatWindow);
  document.body.appendChild(popup);

  chatButton.onclick = () => {
    chatWindow.style.display = chatWindow.style.display === "none" ? "flex" : "none";
    popup.style.display = "block";
  };

  document.getElementById("chat-input").addEventListener("keypress", async function (e) {
    if (e.key === "Enter") {
      const msg = this.value;
      if (!msg.trim()) return;
      const log = document.getElementById("chat-log");
      log.innerHTML += "<div><b>You:</b> " + msg + "</div>";
      this.value = "";

      try {
        const res = await fetch(OPENAI_ENDPOINT, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${OPENAI_API_KEY}`
          },
          body: JSON.stringify({
            model: OPENAI_MODEL,
            messages: [
              { role: "system", content: "You are a helpful Jenkins assistant." },
              { role: "user", content: msg }
            ]
          })
        });

        const data = await res.json();
        if (res.status === 429) {
          log.innerHTML += `<div style="color:red;"><b>Bot:</b> Rate limit exceeded (429)</div>`;
        } else if (data.choices && data.choices.length > 0) {
          log.innerHTML += "<div><b>Bot:</b> " + data.choices[0].message.content + "</div>";
        } else {
          log.innerHTML += `<div style="color:red;"><b>Bot:</b> No response received.</div>`;
        }

        log.scrollTop = log.scrollHeight;

      } catch (err) {
        log.innerHTML += `<div style="color:red;"><b>Error:</b> ${err.message}</div>`;
      }
    }
  });

  document.getElementById("close-popup").onclick = () => {
    popup.style.display = "none";
  };
});
