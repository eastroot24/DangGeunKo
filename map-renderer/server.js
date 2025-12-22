console.log("server.js starting...");

const express = require("express");
const { chromium } = require("playwright");


const app = express();
app.use(express.json({ limit: "1mb" }));
app.use("/static", express.static("public"));
const NAVER_MAP_CLIENT_ID = process.env.NAVER_MAP_CLIENT_ID;
if (!NAVER_MAP_CLIENT_ID) {
  console.error("❌ Missing NAVER_MAP_CLIENT_ID");
  process.exit(1);
}

console.log("NAVER_MAP_CLIENT_ID =", NAVER_MAP_CLIENT_ID);

/* =====================================================
   1) 지도 HTML 생성
===================================================== */
function buildHtml({ points, width, height }) {
  return `<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <style>
    html, body { margin:0; padding:0; }
    #map { width:${width}px; height:${height}px; }
    .normal-marker {
      width:6px;
      height:6px;
      background:#ffffff;
      border-radius:50%;
      border:3px solid #ff8a24;
    }
  </style>
</head>
<body>
  <div id="map"></div>

  <script>
    window.POINTS = ${JSON.stringify(points)};
    window.DONE = false;
    window.ERROR = null;
  </script>

  <script src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${NAVER_MAP_CLIENT_ID}"></script>

  <script>
    try {
      const pts = window.POINTS;
      if (!pts || pts.length === 0) {
        window.ERROR = "No points";
        window.DONE = true;
      }

      const latlngs = pts.map(p => new naver.maps.LatLng(p.lat, p.lng));

      const bounds = new naver.maps.LatLngBounds(latlngs[0], latlngs[0]);
      latlngs.forEach(ll => bounds.extend(ll));

      const map = new naver.maps.Map("map", {
        center: latlngs[0],
        zoomControl: false,
        mapDataControl: false,
        scaleControl: false,
        logoControl: false
      });

      map.fitBounds(bounds, { top: 20, bottom: 20, left: 20, right: 20 });

      const START_ICON = {
        url: "http://localhost:4001/static/dgk_marker.png",
        size: new naver.maps.Size(40, 40),
        scaledSize: new naver.maps.Size(40, 40),
        anchor: new naver.maps.Point(20, 40)
      };

      const NORMAL_ICON = {
        content: '<div class="normal-marker"></div>',
        anchor: new naver.maps.Point(6, 6)
      };

      // 마커 생성
      latlngs.forEach((ll, idx) => {
        new naver.maps.Marker({
          position: ll,
          map,
          icon: idx === 0 ? START_ICON : NORMAL_ICON
        });
      });

      // 폴리라인
      if (latlngs.length >= 2) {
        new naver.maps.Polyline({
          map,
          path: latlngs,
          strokeColor: "#ff7a00",
          strokeWeight: 4
        });
      }

      setTimeout(() => window.DONE = true, 800);

    } catch (e) {
      window.ERROR = e.toString();
      window.DONE = true;
    }
  </script>
</body>
</html>`;
}

/* =====================================================
   2) HTML 제공 (네이버 인증 통과용)
===================================================== */
app.get("/map", (req, res) => {
  const points = JSON.parse(req.query.points || "[]");
  const width = Number(req.query.w || 400);
  const height = Number(req.query.h || 300);

  res.setHeader("Content-Type", "text/html; charset=utf-8");
  res.send(buildHtml({ points, width, height }));
});

/* =====================================================
   3) 이미지 렌더 API
===================================================== */
app.post("/render", async (req, res) => {
  const { points, width = 400, height = 300 } = req.body;

  if (!points || points.length === 0) {
    return res.status(400).json({ message: "points required" });
  }

  const url =
    "http://localhost:4001/map" +
    "?w=" + width +
    "&h=" + height +
    "&points=" + encodeURIComponent(JSON.stringify(points));

  let browser;
  try {
    browser = await chromium.launch({ headless: true });
    const page = await browser.newPage({
      viewport: { width, height },
      deviceScaleFactor: 2
    });

    await page.goto(url, { waitUntil: "networkidle" });
    await page.waitForFunction(() => window.DONE === true, { timeout: 15000 });

    const error = await page.evaluate(() => window.ERROR);
    if (error) throw new Error(error);

    const mapEl = await page.$("#map");
    const png = await mapEl.screenshot({ type: "png" });

    await browser.close();

    res.setHeader("Content-Type", "image/png");
    res.send(png);

  } catch (e) {
    if (browser) await browser.close();
    console.error(e);
    res.status(500).json({ message: e.toString() });
  }
});

/* =====================================================
   4) 서버 실행 (한 번만!)
===================================================== */
app.listen(4001, () => {
  console.log("✅ map-renderer running at http://localhost:4001");
});
