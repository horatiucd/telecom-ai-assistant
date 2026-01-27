(() => {
  "use strict";

  // auto-scroll to the bottom after load
  const chatWindow = document.getElementById("chatWindow");
  if (chatWindow) {
    chatWindow.scrollTop = chatWindow.scrollHeight;
  }

  const form = document.getElementById("chatForm");
  const btn = document.getElementById("sendBtn");
  const input = document.getElementById("questionInput");
  const hint = document.getElementById("sendHint");

  if (!form || !btn || !input) return;

  // initial state - covers browser autofill/back navigation
  const setReadyState = () => {
    const hasText = input.value.trim().length > 0;
    btn.disabled = !hasText;
  };

  setReadyState();

  // enable/disable Send as the user types
  input.addEventListener("input", setReadyState);

  // disable send button + show progress until server responds (page reload)
  form.addEventListener("submit", (e) => {
    // don't submit blanks (should be impossible due to disabled button, but covers Enter key).
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
    if (hint) hint.classList.remove("d-none");
  });
})();
