(() => {
  "use strict";

  const chatWindow = document.getElementById("chatWindow");
  if (chatWindow) {
    chatWindow.scrollTop = chatWindow.scrollHeight;
  }

  const form = document.getElementById("chatForm");
  const btn = document.getElementById("sendBtn");
  const input = document.getElementById("questionInput");

  if (!form || !btn || !input) return;

  const setReadyState = () => {
    const hasText = input.value.trim().length > 0;
    btn.disabled = !hasText;
  };

  setReadyState();

  input.addEventListener("input", setReadyState);

  form.addEventListener("submit", (e) => {
    if (input.value.trim().length === 0) {
      e.preventDefault();
      setReadyState();
      input.focus();
      return;
    }

    btn.disabled = true;
    input.readOnly = true;

    const label = btn.querySelector(".send-label");
    const progress = btn.querySelector(".send-progress");

    if (label) label.classList.add("d-none");
    if (progress) progress.classList.remove("d-none");
  });
})();
